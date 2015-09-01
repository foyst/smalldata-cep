package uk.co.foyst.smalldata.cep.dao;

import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;

public abstract class EventConsumerConfigViewFactory {

    protected final StreamViewFactory streamViewFactory;

    public EventConsumerConfigViewFactory(StreamViewFactory streamViewFactory) {
        this.streamViewFactory = streamViewFactory;
    }

    abstract EventConsumerConfigView build(final EventConsumerConfig eventConsumerConfig);

    abstract EventConsumerConfig convertToEventConsumerConfig(final EventConsumerConfigView eventConsumerConfigView);
}
