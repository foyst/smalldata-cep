package uk.co.foyst.smalldata.cep.api.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;

import java.util.ArrayList;
import java.util.List;

@Component
public class AbstractEventConsumerConfigDtoFactory {

    private final List<EventConsumerConfigDtoFactory> eventConsumerConfigDtoFactories;

    @Autowired
    public AbstractEventConsumerConfigDtoFactory(final List<EventConsumerConfigDtoFactory> configDtoFactories) {

        this.eventConsumerConfigDtoFactories = configDtoFactories;
    }

    public EventConsumerConfigDto build(final EventConsumerConfig eventConsumerConfig) {

        EventConsumerConfigDto eventConsumerConfigDto = null;
        for (final EventConsumerConfigDtoFactory configDtoFactory : eventConsumerConfigDtoFactories) {

            eventConsumerConfigDto = configDtoFactory.build(eventConsumerConfig);
            if (eventConsumerConfigDto != null)
                break;
        }

        if (eventConsumerConfigDto == null)
            throw new IllegalArgumentException("DtoFactory not found for Config Type: " + eventConsumerConfig.getClass().getSimpleName());

        return eventConsumerConfigDto;
    }

    public List<EventConsumerConfigDto> build(final List<EventConsumerConfig> eventConsumerConfigs) {

        final List<EventConsumerConfigDto> builtEventConsumerConfigDtos = new ArrayList<>(eventConsumerConfigs.size());
        for (final EventConsumerConfig eventConsumerConfig : eventConsumerConfigs)
            builtEventConsumerConfigDtos.add(build(eventConsumerConfig));

        return builtEventConsumerConfigDtos;
    }

    public EventConsumerConfig convertToEventConsumerConfig(final EventConsumerConfigDto eventConsumerConfigDto) {

        EventConsumerConfig eventConsumerConfig = null;
        for (final EventConsumerConfigDtoFactory configDtoFactory : eventConsumerConfigDtoFactories) {

            eventConsumerConfig = configDtoFactory.convertToEventConsumerConfig(eventConsumerConfigDto);
            if (eventConsumerConfig != null)
                break;
        }

        if (eventConsumerConfig == null)
            throw new IllegalArgumentException("DtoFactory not found for Config Type: " + eventConsumerConfigDto.getClass().getSimpleName());

        return eventConsumerConfig;
    }
}
