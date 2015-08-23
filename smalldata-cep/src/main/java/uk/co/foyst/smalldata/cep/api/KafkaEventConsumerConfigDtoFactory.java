package uk.co.foyst.smalldata.cep.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.StreamId;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;
import uk.co.foyst.smalldata.cep.consumer.KafkaEventConsumerConfig;
import uk.co.foyst.smalldata.cep.service.StreamService;

import java.util.HashMap;
import java.util.Map;

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

        return eventConsumerConfigDto.getConsumerType().equals(KAFKA_CONSUMER_TYPE);
    }

    @Override
    public EventConsumerConfigDto build(final EventConsumerConfig eventConsumerConfig) {

        final String streamId = eventConsumerConfig.getInputStream().getStreamId().toString();
        final KafkaEventConsumerConfig kafkaEventConsumerConfig = (KafkaEventConsumerConfig) eventConsumerConfig;
        final Map<String, String> kafkaConfigProperties = new HashMap<>();
        kafkaConfigProperties.put(GROUP_ID_KEY, kafkaEventConsumerConfig.getGroupId());
        kafkaConfigProperties.put(TOPIC_KEY, kafkaEventConsumerConfig.getTopic());
        kafkaConfigProperties.put(ZOOKEEPER_URL_KEY, kafkaEventConsumerConfig.getZookeeperUrl());
        kafkaConfigProperties.put(POOL_SIZE_KEY, kafkaEventConsumerConfig.getPoolSize().toString());

        return new EventConsumerConfigDto(eventConsumerConfig.getEventConsumerId().toString(), streamId, KAFKA_CONSUMER_TYPE, kafkaConfigProperties);
    }

    @Override
    public EventConsumerConfig convertToEventConsumerConfig(EventConsumerConfigDto eventConsumerConfigDto) {

        EventConsumerId eventConsumerId = EventConsumerId.fromString(eventConsumerConfigDto.getEventConsumerId());
        Stream inputStream = streamService.read(StreamId.fromString(eventConsumerConfigDto.getStreamId()));
        final Map<String, String> configProperties = eventConsumerConfigDto.getConfigProperties();
        String zookeeperUrl = configProperties.get(ZOOKEEPER_URL_KEY);
        String groupId = configProperties.get(GROUP_ID_KEY);
        String topic = configProperties.get(TOPIC_KEY);
        int poolSize = Integer.parseInt(configProperties.get(POOL_SIZE_KEY));
        return new KafkaEventConsumerConfig(eventConsumerId, inputStream, zookeeperUrl, groupId, topic, poolSize);
    }
}