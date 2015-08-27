package uk.co.foyst.smalldata.cep.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.StreamId;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;
import uk.co.foyst.smalldata.cep.consumer.KafkaEventConsumerConfig;
import uk.co.foyst.smalldata.cep.service.StreamService;

@Component
public class KafkaEventConsumerConfigDtoFactory implements EventConsumerConfigDtoFactory {

    public static final String KAFKA_CONSUMER_TYPE = "KAFKA";
    public static final String ZOOKEEPER_URL_KEY = "zookeeperUrl";
    public static final String GROUP_ID_KEY = "groupId";
    public static final String TOPIC_KEY = "topic";
    public static final String POOL_SIZE_KEY = "poolSize";

    private final StreamService streamService;

    @Autowired
    public KafkaEventConsumerConfigDtoFactory(StreamService streamService) {
        this.streamService = streamService;
    }

    @Override
    public boolean compatibleWith(EventConsumerConfig eventConsumerConfig) {

        return eventConsumerConfig instanceof KafkaEventConsumerConfig;
    }

    @Override
    public boolean compatibleWith(EventConsumerConfigDto eventConsumerConfigDto) {

        return eventConsumerConfigDto instanceof KafkaEventConsumerConfigDto;
    }

    @Override
    public EventConsumerConfigDto build(final EventConsumerConfig eventConsumerConfig) {

        final String streamId = eventConsumerConfig.getInputStream().getStreamId().toString();
        final KafkaEventConsumerConfig kafkaEventConsumerConfig = (KafkaEventConsumerConfig) eventConsumerConfig;
        final String groupId = kafkaEventConsumerConfig.getGroupId();
        final String topic = kafkaEventConsumerConfig.getTopic();
        final String zookeeperUrl = kafkaEventConsumerConfig.getZookeeperUrl();

        return new KafkaEventConsumerConfigDto(eventConsumerConfig.getEventConsumerId().toString(), streamId, zookeeperUrl, groupId, topic);
    }

    @Override
    public EventConsumerConfig convertToEventConsumerConfig(EventConsumerConfigDto eventConsumerConfigDto) {

        EventConsumerId eventConsumerId = EventConsumerId.fromString(eventConsumerConfigDto.getEventConsumerId());
        Stream inputStream = streamService.read(StreamId.fromString(eventConsumerConfigDto.getStreamId()));
        final KafkaEventConsumerConfigDto kafkaEventConsumerConfigDto = (KafkaEventConsumerConfigDto) eventConsumerConfigDto;
        final String zookeeperUrl = kafkaEventConsumerConfigDto.getZookeeperUrl();
        final String groupId = kafkaEventConsumerConfigDto.getGroupId();
        final String topic = kafkaEventConsumerConfigDto.getTopic();

        return new KafkaEventConsumerConfig(eventConsumerId, inputStream, zookeeperUrl, groupId, topic);
    }
}