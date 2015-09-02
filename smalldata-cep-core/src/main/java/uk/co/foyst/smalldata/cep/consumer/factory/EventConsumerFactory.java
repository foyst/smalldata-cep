package uk.co.foyst.smalldata.cep.consumer.factory;

import uk.co.foyst.smalldata.cep.consumer.EventConsumer;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;

public interface EventConsumerFactory {
    EventConsumer build(final EventConsumerConfig eventConsumerConfig);
}
