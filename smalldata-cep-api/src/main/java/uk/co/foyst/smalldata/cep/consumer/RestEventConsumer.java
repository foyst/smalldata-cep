package uk.co.foyst.smalldata.cep.consumer;

import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.adapter.CEPAdapter;

public class RestEventConsumer implements EventConsumer {

    private final EventConsumerId eventConsumerId;
    private final CEPAdapter cepAdapter;
    private final Stream inputStream;
    private final RestEventConsumerController consumerController;

    private boolean started;

    public RestEventConsumer(final RestEventConsumerConfig config, final CEPAdapter cepAdapter, final RestEventConsumerController consumerController) {
        this.eventConsumerId = config.getEventConsumerId();
        this.cepAdapter = cepAdapter;
        this.inputStream = config.getInputStream();
        this.consumerController = consumerController;
    }

    @Override
    public void start() {

        consumerController.registerEventConsumer(this);
        started = true;
    }

    @Override
    public void stop() {

        consumerController.unregisterEventConsumer(this);
        started = false;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    public String getInputStreamName() {
        return inputStream.getName();
    }

    public void sendEvent(final Object[] eventAttributes) throws InterruptedException {

        cepAdapter.sendEvent(inputStream, eventAttributes);
    }
}
