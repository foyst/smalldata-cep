package uk.co.foyst.smalldata.cep.testfactory;

import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;
import uk.co.foyst.smalldata.cep.dao.EventConsumerConfigView;
import uk.co.foyst.smalldata.cep.dao.StreamView;

import java.util.HashMap;
import java.util.Map;

public class EventConsumerConfigViewTestFactory {

    public static EventConsumerConfigView buildTestKafkaConsumerConfigView() {

        final EventConsumerId eventConsumerId = new EventConsumerId();
        final String zookeeperUrl = "localhost:8080";
        final String groupId = "testGroup";
        final String topic = "testTopic";
        final Integer poolSize = 5;

        final Map<String, String> kafkaConfigProperties = new HashMap<>();
        kafkaConfigProperties.put("zookeeperUrl", zookeeperUrl);
        kafkaConfigProperties.put("groupId", groupId);
        kafkaConfigProperties.put("topic", topic);
        kafkaConfigProperties.put("poolSize", poolSize.toString());
        kafkaConfigProperties.put("messageTransformer", "ORDERED_JSON");

        final StreamView streamView = StreamViewTestFactory.buildTestInputStream();

        return new EventConsumerConfigView(eventConsumerId.toString(), streamView, "KAFKA", kafkaConfigProperties);
    }
}
