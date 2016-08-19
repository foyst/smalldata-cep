package uk.co.foyst.smalldata.cep.api.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.StreamId;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;
import uk.co.foyst.smalldata.cep.consumer.KafkaEventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.MessageTransformer;
import uk.co.foyst.smalldata.cep.service.StreamService;

@Component
public class KafkaEventConsumerConfigDtoFactory implements EventConsumerConfigDtoFactory {

    private final StreamService streamService;

    @Autowired
    public KafkaEventConsumerConfigDtoFactory(StreamService streamService) {
        this.streamService = streamService;
    }

    @Override
    public EventConsumerConfigDto build(final EventConsumerConfig eventConsumerConfig) {

        KafkaEventConsumerConfigDto consumerConfigDto = null;

        if (eventConsumerConfig instanceof KafkaEventConsumerConfig) {

            final String streamId = eventConsumerConfig.getInputStream().getStreamId().toString();
            final KafkaEventConsumerConfig kafkaEventConsumerConfig = (KafkaEventConsumerConfig) eventConsumerConfig;
            final String groupId = kafkaEventConsumerConfig.getGroupId();
            final String topic = kafkaEventConsumerConfig.getTopic();
            final String zookeeperUrl = kafkaEventConsumerConfig.getZookeeperUrl();
            final String messageTransformer = "orderedJson";
            consumerConfigDto = new KafkaEventConsumerConfigDto(eventConsumerConfig.getEventConsumerId().toString(), streamId, zookeeperUrl, groupId, topic, messageTransformer);
        }

        return consumerConfigDto;
    }

    @Override
    public EventConsumerConfig convertToEventConsumerConfig(EventConsumerConfigDto eventConsumerConfigDto) {

        KafkaEventConsumerConfig consumerConfig = null;

        if (eventConsumerConfigDto instanceof KafkaEventConsumerConfigDto) {
            EventConsumerId eventConsumerId = EventConsumerId.fromString(eventConsumerConfigDto.getEventConsumerId());
            Stream inputStream = streamService.read(StreamId.fromString(eventConsumerConfigDto.getStreamId()));
            final KafkaEventConsumerConfigDto kafkaEventConsumerConfigDto = (KafkaEventConsumerConfigDto) eventConsumerConfigDto;
            final String zookeeperUrl = kafkaEventConsumerConfigDto.getZookeeperUrl();
            final String groupId = kafkaEventConsumerConfigDto.getGroupId();
            final String topic = kafkaEventConsumerConfigDto.getTopic();
            final MessageTransformer messageTransformer = MessageTransformer.valueOf(kafkaEventConsumerConfigDto.getMessageTransformer());
            consumerConfig = new KafkaEventConsumerConfig(eventConsumerId, inputStream, zookeeperUrl, groupId, topic, messageTransformer);
        }

        return consumerConfig;
    }
}