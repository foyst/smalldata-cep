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
public class KafkaEventConsumerConfigViewFactory extends EventConsumerConfigViewFactory {

    private static final String KAFKA_CONSUMER_TYPE = "KAFKA";
    private static final String ZOOKEEPER_URL_KEY = "zookeeperUrl";
    private static final String GROUP_ID_KEY = "groupId";
    private static final String TOPIC_KEY = "topic";

    @Autowired
    public KafkaEventConsumerConfigViewFactory(StreamViewFactory streamViewFactory) {
        super(streamViewFactory);
    }

    @Override
    public EventConsumerConfigView build(final EventConsumerConfig eventConsumerConfig) {

        EventConsumerConfigView eventConsumerConfigView = null;
        if (eventConsumerConfig instanceof KafkaEventConsumerConfig) {
            final StreamView streamView = streamViewFactory.build(eventConsumerConfig.getInputStream());

            final KafkaEventConsumerConfig kafkaEventConsumerConfig = (KafkaEventConsumerConfig) eventConsumerConfig;
            final Map<String, String> kafkaConfigProperties = new HashMap<>();
            kafkaConfigProperties.put(GROUP_ID_KEY, kafkaEventConsumerConfig.getGroupId());
            kafkaConfigProperties.put(TOPIC_KEY, kafkaEventConsumerConfig.getTopic());
            kafkaConfigProperties.put(ZOOKEEPER_URL_KEY, kafkaEventConsumerConfig.getZookeeperUrl());

            eventConsumerConfigView = new EventConsumerConfigView(eventConsumerConfig.getEventConsumerId().toString(), streamView, KAFKA_CONSUMER_TYPE, kafkaConfigProperties);
        }
        return eventConsumerConfigView;
    }

    @Override
    public EventConsumerConfig convertToEventConsumerConfig(EventConsumerConfigView eventConsumerConfigView) {

        EventConsumerConfig eventConsumerConfig = null;
        if (eventConsumerConfigView.getConsumerType().equals(KAFKA_CONSUMER_TYPE)) {
            EventConsumerId eventConsumerId = EventConsumerId.fromString(eventConsumerConfigView.getEventConsumerId());
            Stream inputStream = streamViewFactory.convertToStream(eventConsumerConfigView.getStreamView());
            final Map<String, String> configProperties = eventConsumerConfigView.getConfigProperties();
            String zookeeperUrl = configProperties.get(ZOOKEEPER_URL_KEY);
            String groupId = configProperties.get(GROUP_ID_KEY);
            String topic = configProperties.get(TOPIC_KEY);
            eventConsumerConfig = new KafkaEventConsumerConfig(eventConsumerId, inputStream, zookeeperUrl, groupId, topic);
        }
        return eventConsumerConfig;
    }
}