package uk.co.foyst.smalldata.cep.dao;

import org.junit.Before;
import org.junit.Test;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;
import uk.co.foyst.smalldata.cep.consumer.KafkaEventConsumerConfig;
import uk.co.foyst.smalldata.cep.testfactory.EventConsumerConfigViewTestFactory;
import uk.co.foyst.smalldata.cep.testfactory.StreamTestFactory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class KafkaEventConsumerConfigViewFactoryTests {

    private StreamViewFactory streamViewFactory;
    private KafkaEventConsumerConfigViewFactory viewFactory;

    @Before
    public void setUp() {

        streamViewFactory = new StreamViewFactory();
        viewFactory = new KafkaEventConsumerConfigViewFactory(streamViewFactory);
    }

    @Test
    public void shouldBuildConfigViewGivenKafkaEventConsumerConfig() {

        // Arrange
        final EventConsumerId eventConsumerId = new EventConsumerId();
        final Stream inputStream = StreamTestFactory.buildTestInputStream();
        final String zookeeperUrl = "localhost:2821";
        final String groupId = "TestGroupId";
        final String topic = "TestTopic";
        final EventConsumerConfig kafkaEventConsumerConfig = new KafkaEventConsumerConfig(eventConsumerId, inputStream, zookeeperUrl, groupId, topic);

        // Act
        final EventConsumerConfigView eventConsumerConfigView = viewFactory.build(kafkaEventConsumerConfig);

        // Assert
        assertEquals(eventConsumerId.toString(), eventConsumerConfigView.getEventConsumerId());
        assertEquals("KAFKA", eventConsumerConfigView.getConsumerType());
        assertEquals(zookeeperUrl, eventConsumerConfigView.getConfigProperties().get("zookeeperUrl"));
        assertEquals(groupId, eventConsumerConfigView.getConfigProperties().get("groupId"));
        assertEquals(topic, eventConsumerConfigView.getConfigProperties().get("topic"));
    }

    @Test
    public void shouldBeCompatibleWithKafkaEventConsumerConfigClass() {

        assertEquals(true, viewFactory.compatibleWith(mock(KafkaEventConsumerConfig.class)));
    }

    @Test
    public void shouldBuildKafkaEventConsumerConfigGivenEventConsumerConfigView() {

        // Arrange
        final EventConsumerConfigView eventConsumerConfigView = EventConsumerConfigViewTestFactory.buildTestKafkaConsumerConfigView();

        // Act
        final KafkaEventConsumerConfig consumerConfig = (KafkaEventConsumerConfig) viewFactory.convertToEventConsumerConfig(eventConsumerConfigView);

        // Assert
        assertEquals(eventConsumerConfigView.getEventConsumerId(), consumerConfig.getEventConsumerId().toString());
        assertEquals(eventConsumerConfigView.getStreamView().getStreamId(), consumerConfig.getInputStream().getStreamId().toString());
        assertEquals(eventConsumerConfigView.getStreamView().getName(), consumerConfig.getInputStream().getName());
        assertEquals(eventConsumerConfigView.getStreamView().getDescription(), consumerConfig.getInputStream().getDescription());
        assertEquals(eventConsumerConfigView.getStreamView().getDefinition(), consumerConfig.getInputStream().getDefinition());
        assertEquals(eventConsumerConfigView.getConfigProperties().get("zookeeperUrl"), consumerConfig.getZookeeperUrl());
        assertEquals(eventConsumerConfigView.getConfigProperties().get("groupId"), consumerConfig.getGroupId());
        assertEquals(eventConsumerConfigView.getConfigProperties().get("topic"), consumerConfig.getTopic());
        assertEquals(eventConsumerConfigView.getConfigProperties().get("poolSize"), consumerConfig.getPoolSize().toString());
    }
}