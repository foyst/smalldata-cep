package uk.co.foyst.smalldata.cep.consumer.factory;

import uk.co.foyst.smalldata.cep.consumer.EventConsumer;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;

public interface EventConsumerFactory<T extends EventConsumerConfig> {
    EventConsumer build(T eventConsumerConfig);
}
