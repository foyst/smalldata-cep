package uk.co.foyst.smalldata.cep.dao;

import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;

public interface EventConsumerConfigViewFactory {

    EventConsumerConfigView build(final EventConsumerConfig eventConsumerConfig);

    EventConsumerConfig convertToEventConsumerConfig(final EventConsumerConfigView eventConsumerConfigView);

    boolean compatibleWith(EventConsumerConfig eventConsumerConfig);

    boolean compatibleWith(EventConsumerConfigView eventConsumerConfigView);
}
