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
import uk.co.foyst.smalldata.cep.CEPEvent;
import uk.co.foyst.smalldata.cep.CEPEventObserver;
import uk.co.foyst.smalldata.cep.Scenario;
import uk.co.foyst.smalldata.cep.Stream;

import java.util.*;

@Service
public class SiddhiCEPAdapter implements CEPAdapter {

    Logger log = LoggerFactory.getLogger(this.getClass());

    protected SiddhiManager manager;

    protected final Multimap<Scenario, String> queries = HashMultimap.create();
    protected final HashMap<String, String> streams = new HashMap<>();
    protected final Multimap<Scenario, String> partitions = HashMultimap.create();
    protected final Multimap<Scenario, String> tables = HashMultimap.create();

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

    private void addQueryCallback(final String queryId, final CEPEventObserver cepEventObserver) {

        log.debug("Attaching QueryCallback {} to Query {}", cepEventObserver, queryId);

        manager.addCallback(queryId, new QueryCallback() {
            @Override
            public void receive(final long timeStamp, final Event[] inEvents, final Event[] removeEvents) {
                cepEventObserver.receive(convert(inEvents));
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
    public void sendEvents(final Stream stream, final Object[][] events) {

        for (final Object[] event : events) {
            sendEvent(stream, event);
        }
    }

    @Override
    public void sendEvent(final Stream stream, final Object[] event) {

        final String internalStreamId = this.streams.get(stream.getStreamId().toString());
        final InputHandler inputHandler = manager.getInputHandler(internalStreamId);
        try {
            inputHandler.send(event);
        } catch (InterruptedException e) {
            throw new CEPAdapterException(String.format("Exception attempting to send event %s to Stream %s", event,
                    stream.getStreamId().toString()), e);
        }
    }

    @Override
    public void define(final Scenario scenario) {

        try {
            defineExecutionPlan(scenario);
            //FIXME: Need to tidy up and verify thrown Exceptions (it's not just SiddhiParserExceptions that can be
            //thrown here
        } catch (final SiddhiParserException ex) {
            remove(scenario);
            throw ex;
        }
    }

    protected void defineExecutionPlan(final Scenario scenario) {

        log.debug("defineExecutionPlan: scenario={}", scenario);

        String queryDefinition = scenario.getDefinition();
        final List<ExecutionPlan> executionPlanList = SiddhiCompiler.parse(queryDefinition);
        log.trace("defineExecutionPlan: executionPlanList={}", executionPlanList);

        for (final ExecutionPlan plan : executionPlanList) {
            if (plan instanceof Query) {
                defineQuery(scenario, plan);
            } else if (plan instanceof StreamDefinition) {
                defineQueryStream(plan);
            } else if (plan instanceof TableDefinition) {
                defineTable(scenario, plan);
            } else if (plan instanceof PartitionDefinition) {
                definePartition(scenario, plan);
            } else {
                throw new CEPAdapterException("Error parsing " + queryDefinition + ": Unknown ExecutionPlan Type found in query");
            }
        }

        log.info("Scenario with Id '" + scenario.getScenarioId().toString() + "' successfully created");
    }

    private void definePartition(Scenario scenario, ExecutionPlan plan) {
        log.debug("plan is a PartitionDefinition: {}", plan);
        manager.definePartition(((PartitionDefinition) plan));
        partitions.put(scenario, ((PartitionDefinition) plan).getPartitionId());
    }

    private void defineTable(Scenario scenario, ExecutionPlan plan) {
        log.debug("plan is a TableDefinition: {}", plan);
        manager.defineTable((TableDefinition) plan);
        tables.put(scenario, ((TableDefinition) plan).getTableId());
    }

    private void defineQueryStream(ExecutionPlan plan) {
        log.debug("plan is a StreamDefinition: {}", plan);
        final StreamDefinition streamDefinition = (StreamDefinition) plan;
        final InputHandler handler = manager.defineStream(streamDefinition);
        streams.put(handler.getStreamId(), handler.getStreamId());
    }

    private void defineQuery(Scenario scenario, ExecutionPlan plan) {
        log.debug("plan is a Query: {}", plan);
        try {
            final String queryId = manager.addQuery((Query) plan);
            queries.put(scenario, queryId);
            updateStreamListeners(queryId);
        } catch (final QueryCreationException | DifferentDefinitionAlreadyExistException ex) {
            throw new CEPAdapterException("Unable to create Scenario with Id '" + scenario.getScenarioId().toString() + "'", ex);
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

    protected void updateStreamListeners(final String queryId) {

        for (final CEPEventObserver listener : listeners) {
            addQueryCallback(queryId, listener);
        }
    }
}
