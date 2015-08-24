package uk.co.foyst.smalldata.cep.api;

import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;

public interface EventConsumerConfigDtoFactory {

    EventConsumerConfigDto build(final EventConsumerConfig eventConsumerConfig);

    EventConsumerConfig convertToEventConsumerConfig(final EventConsumerConfigDto eventConsumerConfigDto);

    boolean compatibleWith(EventConsumerConfig eventConsumerConfig);

    boolean compatibleWith(EventConsumerConfigDto eventConsumerConfigDto);
}
