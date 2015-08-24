package uk.co.foyst.smalldata.cep.consumer;

import uk.co.foyst.smalldata.cep.Stream;

public abstract class EventConsumerConfig {

    private final EventConsumerId eventConsumerId;
    private final Stream inputStream;

    public EventConsumerConfig(EventConsumerId eventConsumerId, Stream inputStream) {
        this.eventConsumerId = eventConsumerId;
        this.inputStream = inputStream;
    }

    public EventConsumerId getEventConsumerId() {
        return eventConsumerId;
    }

    public Stream getInputStream() {
        return inputStream;
    }
}
