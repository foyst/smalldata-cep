package uk.co.foyst.smalldata.cep.adapter;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.DifferentDefinitionAlreadyExistException;
import org.wso2.siddhi.core.exception.QueryCreationException;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.query.api.ExecutionPlan;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.api.definition.TableDefinition;
import org.wso2.siddhi.query.api.definition.partition.PartitionDefinition;
import org.wso2.siddhi.query.api.query.Query;
import org.wso2.siddhi.query.compiler.SiddhiCompiler;
import org.wso2.siddhi.query.compiler.exception.SiddhiParserException;
import uk.co.foyst.smalldata.cep.*;

import java.util.*;

@Service
public class SiddhiCEPAdapter implements CEPAdapter {

    Logger log = LoggerFactory.getLogger(this.getClass());

    protected SiddhiManager manager;

    protected final Multimap<Scenario, String> queries = HashMultimap.create();
    protected final HashMap<String, String> streams = new HashMap<>();
    protected final Multimap<Scenario, String> partitions = HashMultimap.create();
    protected final Multimap<Scenario, String> tables = HashMultimap.create();

    protected final List<String> scenarioIds = new ArrayList<>();
    protected final List<CEPEventObserver> listeners = new ArrayList<CEPEventObserver>();

    @Autowired
    public SiddhiCEPAdapter(final SiddhiManager manager) {

        this.manager = manager;
    }

    @Override
    public void define(final Stream stream) {

        final InputHandler handler = manager.defineStream(stream.getDefinition());
        streams.put(stream.getStreamId().toString(), handler.getStreamId());
    }

    @Override
    public void update(Stream updatedStream) {

        manager.removeStream(updatedStream.getStreamId().toString());
        manager.defineStream(updatedStream.getDefinition());
    }

    @Override
    public void remove(final Stream stream) {

        final String streamIdString = stream.getStreamId().toString();
        final String internalStreamId = streams.get(streamIdString);
        manager.removeStream(internalStreamId);
        streams.remove(streamIdString);
    }

    @Override
    public void addStreamListener(final CEPEventObserver listener) {

        for (final String queryId : queries.values()) {
            addQueryCallback(queryId, listener);
        }

        listeners.add(listener);
    }

    private void addQueryCallback(final String queryId, final CEPEventObserver listener) {

        log.info("Defining listener for the query (id) : " + queryId);

        manager.addCallback(queryId, new QueryCallback() {
            @Override
            public void receive(final long timeStamp, final Event[] inEvents, final Event[] removeEvents) {

                listener.receive(convert(inEvents));
            }
        });
    }

    CEPEvent[] convert(final Event[] events) {

        final List<CEPEvent> cepEvents = new ArrayList<CEPEvent>();

        for (final Event event : events) {
            final Object[] data = Arrays.copyOf(event.getData(), event.getData().length);
            cepEvents.add(new CEPEvent(event.getStreamId(), event.getTimeStamp(), data));
        }

        return cepEvents.toArray(new CEPEvent[cepEvents.size()]);
    }

    @Override
    public void sendEvents(final Stream stream, final Object[][] events) throws InterruptedException {

        for (final Object[] event : events) {
            sendEvent(stream, event);
        }
    }

    @Override
    public void sendEvent(final Stream stream, final Object[] event) throws InterruptedException {

        final String internalStreamId = this.streams.get(stream.getStreamId().toString());
        final InputHandler inputHandler = manager.getInputHandler(internalStreamId);
        inputHandler.send(event);
    }

    @Override
    public void define(final Scenario scenario) {

        try {
            defineExecutionPlan(scenario);
        } catch (final SiddhiParserException ex) {
            remove(scenario);
            throw ex;
        }
    }

    @Override
    public void update(final Scenario scenario) {

        remove(scenario);
        define(scenario);

        log.info("Scenario '" + scenario.getName() + "' updated.");
    }

    @Override
    public void remove(final Scenario scenario) {

        for (final String queryId : queries.get(scenario)) {
            if (!isUsedForOtherScenarios(queryId, scenario, queries)) {
                manager.removeQuery(queryId);
            }
        }

        for (final String tableId : tables.get(scenario)) {
            if (!isUsedForOtherScenarios(tableId, scenario, tables)) {
                manager.removeTable(tableId);
            }
        }

        for (final String partitionId : partitions.get(scenario)) {
            if (!isUsedForOtherScenarios(partitionId, scenario, partitions)) {
                manager.removePartition(partitionId);
            }
        }

        queries.removeAll(scenario);
        tables.removeAll(scenario);
        partitions.removeAll(scenario);
    }

    private boolean isUsedForOtherScenarios(final String executionPlanId, final Scenario scenario, final Multimap<Scenario, String> data) {

        final Set<Scenario> keySet = data.keySet();
        for (final Scenario key : keySet) {
            if (key.getScenarioId().equals(scenario.getScenarioId())) {
                continue;
            }
            final Collection<String> values = data.get(key);
            if (values.contains(executionPlanId)) {
                return true;
            }
        }
        return false;
    }

    protected void defineExecutionPlan(final Scenario scenario) throws SiddhiParserException {

        log.info("defineExecutionPlan: scenario={}", scenario);

        String queryDefinition = scenario.getDefinition();
        log.debug("defineExecutionPlan: scenarioId={}, queryDefinition={}", scenario.getScenarioId(), queryDefinition);

        final List<ExecutionPlan> executionPlanList = SiddhiCompiler.parse(queryDefinition);
        log.debug("defineExecutionPlan: executionPlanList={}", executionPlanList);

        // TODO: Check - do the ExecutionPlans need to be in dependency order
        // (i.e. trying to define a query before a partition that it relies on)
        for (final ExecutionPlan plan : executionPlanList) {
            if (plan instanceof Query) {
                log.debug("plan is a Query: {}", plan);
                try {
                    log.trace("output stream is: {}", ((Query) plan).getSelector().getSelectionList());
                    final String queryId = manager.addQuery((Query) plan);
                    queries.put(scenario, queryId);
                    updateStreamListeners(queryId);
                } catch (final QueryCreationException ex) {
                    throw new SiddhiParserException("Error parsing : " + queryDefinition, ex);
                } catch (final DifferentDefinitionAlreadyExistException e) {

                    throw new SiddhiParserException("A Different Stream Definition already exists. Common causes of this are: \n "
                            + "1: The Select fields don't match the stream in your defined output stream \n "
                            + "2: The datatypes between your output stream and your Select fields don't match", e);
                }
            } else if (plan instanceof StreamDefinition) {
                log.debug("plan is a StreamDefinition: {}", plan);
                final StreamDefinition streamDefinition = (StreamDefinition) plan;
                final InputHandler handler = manager.defineStream(streamDefinition);
                streams.put(handler.getStreamId(), handler.getStreamId());

            } else if (plan instanceof TableDefinition) {
                log.debug("plan is a TableDefinition: {}", plan);
                manager.defineTable((TableDefinition) plan);
                tables.put(scenario, ((TableDefinition) plan).getTableId());

            } else if (plan instanceof PartitionDefinition) {
                log.debug("plan is a PartitionDefinition: {}", plan);
                manager.definePartition(((PartitionDefinition) plan));
                partitions.put(scenario, ((PartitionDefinition) plan).getPartitionId());

            } else {
                throw new SiddhiParserException("Error parsing " + queryDefinition + ": Unknown ExecutionPlan Type found in query");
            }
        }

        log.info("defineExecutionPlan: queries={}, streams={}, tables={}, paritions={}", queries, streams, tables, partitions);
    }

    protected void updateStreamListeners(final String queryId) {

        for (final CEPEventObserver listener : listeners) {
            addQueryCallback(queryId, listener);
        }
    }
}
