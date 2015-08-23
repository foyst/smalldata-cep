package uk.co.foyst.smalldata.cep.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;
import uk.co.foyst.smalldata.cep.consumer.KafkaEventConsumerConfig;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaEventConsumerConfigViewFactory implements EventConsumerConfigViewFactory {

    public static final String KAFKA_CONSUMER_TYPE = "KAFKA";
    public static final String ZOOKEEPER_URL_KEY = "zookeeperUrl";
    public static final String GROUP_ID_KEY = "groupId";
    public static final String TOPIC_KEY = "topic";
    public static final String POOL_SIZE_KEY = "poolSize";

    private final StreamViewFactory streamViewFactory;

    @Autowired
    public KafkaEventConsumerConfigViewFactory(StreamViewFactory streamViewFactory) {
        this.streamViewFactory = streamViewFactory;
    }

    @Override
    public boolean compatibleWith(EventConsumerConfig eventConsumerConfig) {

        return eventConsumerConfig instanceof KafkaEventConsumerConfig;
    }

    @Override
    public boolean compatibleWith(EventConsumerConfigView eventConsumerConfigView) {

        return eventConsumerConfigView.getConsumerType().equals(KAFKA_CONSUMER_TYPE);
    }

    @Override
    public EventConsumerConfigView build(final EventConsumerConfig eventConsumerConfig) {

        final StreamView streamView = streamViewFactory.build(eventConsumerConfig.getInputStream());

        final KafkaEventConsumerConfig kafkaEventConsumerConfig = (KafkaEventConsumerConfig) eventConsumerConfig;
        final Map<String, String> kafkaConfigProperties = new HashMap<>();
        kafkaConfigProperties.put(GROUP_ID_KEY, kafkaEventConsumerConfig.getGroupId());
        kafkaConfigProperties.put(TOPIC_KEY, kafkaEventConsumerConfig.getTopic());
        kafkaConfigProperties.put(ZOOKEEPER_URL_KEY, kafkaEventConsumerConfig.getZookeeperUrl());
        kafkaConfigProperties.put(POOL_SIZE_KEY, kafkaEventConsumerConfig.getPoolSize().toString());

        return new EventConsumerConfigView(eventConsumerConfig.getEventConsumerId().toString(), streamView, KAFKA_CONSUMER_TYPE, kafkaConfigProperties);
    }

    @Override
    public EventConsumerConfig convertToEventConsumerConfig(EventConsumerConfigView eventConsumerConfigView) {

        EventConsumerId eventConsumerId = EventConsumerId.fromString(eventConsumerConfigView.getEventConsumerId());
        Stream inputStream = streamViewFactory.convertToStream(eventConsumerConfigView.getStreamView());
        final Map<String, String> configProperties = eventConsumerConfigView.getConfigProperties();
        String zookeeperUrl = configProperties.get(ZOOKEEPER_URL_KEY);
        String groupId = configProperties.get(GROUP_ID_KEY);
        String topic = configProperties.get(TOPIC_KEY);
        int poolSize = Integer.parseInt(configProperties.get(POOL_SIZE_KEY));
        return new KafkaEventConsumerConfig(eventConsumerId, inputStream, zookeeperUrl, groupId, topic, poolSize);
    }
}