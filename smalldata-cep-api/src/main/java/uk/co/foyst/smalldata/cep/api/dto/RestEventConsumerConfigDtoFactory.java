package uk.co.foyst.smalldata.cep.api.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.StreamId;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;
import uk.co.foyst.smalldata.cep.consumer.RestEventConsumerConfig;
import uk.co.foyst.smalldata.cep.service.StreamService;

@Component
public class RestEventConsumerConfigDtoFactory implements EventConsumerConfigDtoFactory {

    private final StreamService streamService;

    @Autowired
    public RestEventConsumerConfigDtoFactory(StreamService streamService) {
        this.streamService = streamService;
    }

    @Override
    public EventConsumerConfigDto build(final EventConsumerConfig eventConsumerConfig) {

        RestEventConsumerConfigDto consumerConfigDto = null;
        if (eventConsumerConfig instanceof RestEventConsumerConfig) {

            consumerConfigDto = new RestEventConsumerConfigDto(eventConsumerConfig.getEventConsumerId().toString(), eventConsumerConfig.getInputStream().getStreamId().toString());
        }
        return consumerConfigDto;
    }

    @Override
    public EventConsumerConfig convertToEventConsumerConfig(EventConsumerConfigDto eventConsumerConfigDto) {

        RestEventConsumerConfig consumerConfig = null;
        if (eventConsumerConfigDto instanceof RestEventConsumerConfigDto) {

            final Stream stream = streamService.read(StreamId.fromString(eventConsumerConfigDto.getStreamId()));
            final EventConsumerId eventConsumerId = EventConsumerId.fromString(eventConsumerConfigDto.getEventConsumerId());
            consumerConfig = new RestEventConsumerConfig(eventConsumerId, stream);
        }
        return consumerConfig;
    }
}