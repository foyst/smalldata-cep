package uk.co.foyst.smalldata.cep.api.dto;

import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;

public interface EventConsumerConfigDtoFactory {

    EventConsumerConfigDto build(final EventConsumerConfig eventConsumerConfig);

    EventConsumerConfig convertToEventConsumerConfig(final EventConsumerConfigDto eventConsumerConfigDto);
}
