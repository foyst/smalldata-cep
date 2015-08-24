package uk.co.foyst.smalldata.cep.dao;

import org.junit.Before;
import org.junit.Test;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;
import uk.co.foyst.smalldata.cep.consumer.KafkaEventConsumerConfig;
import uk.co.foyst.smalldata.cep.testfactory.EventConsumerConfigViewTestFactory;
import uk.co.foyst.smalldata.cep.testfactory.StreamTestFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AbstractEventConsumerConfigViewFactoryTests {

    private AbstractEventConsumerConfigViewFactory abstractEventConsumerConfigViewFactory;

    @Before
    public void setUp() {

        final StreamViewFactory streamViewFactory = new StreamViewFactory();
        final List<EventConsumerConfigViewFactory> configViewFactories = Arrays.<EventConsumerConfigViewFactory> asList(new KafkaEventConsumerConfigViewFactory(streamViewFactory));
        abstractEventConsumerConfigViewFactory = new AbstractEventConsumerConfigViewFactory(configViewFactories);
    }

    @Test
    public void shouldBuildConsumerConfigViewGivenKafkaConfig() {

        // Arrange
        final EventConsumerId eventConsumerId = new EventConsumerId();
        final Stream inputStream = StreamTestFactory.buildTestInputStream();
        final String zookeeperUrl = "localhost:8080";
        final String groupId = "testGroup";
        final String topic = "testTopic";
        final Integer poolSize = 5;
        final EventConsumerConfig eventConsumerConfig = new KafkaEventConsumerConfig(eventConsumerId, inputStream, zookeeperUrl, groupId, topic, poolSize);

        // Act
        final EventConsumerConfigView consumerConfigView = abstractEventConsumerConfigViewFactory.build(eventConsumerConfig);

        // Assert
        assertEquals(eventConsumerId.toString(), consumerConfigView.getEventConsumerId());
        assertEquals(inputStream.getStreamId().toString(), consumerConfigView.getStreamView().getStreamId());
        assertEquals(inputStream.getName(), consumerConfigView.getStreamView().getName());
        assertEquals(inputStream.getDescription(), consumerConfigView.getStreamView().getDescription());
        assertEquals(inputStream.getDefinition(), consumerConfigView.getStreamView().getDefinition());
        assertEquals("KAFKA", consumerConfigView.getConsumerType());
        assertEquals(zookeeperUrl, consumerConfigView.getConfigProperties().get("zookeeperUrl"));
        assertEquals(groupId, consumerConfigView.getConfigProperties().get("groupId"));
        assertEquals(topic, consumerConfigView.getConfigProperties().get("topic"));
        assertEquals(poolSize.toString(), consumerConfigView.getConfigProperties().get("poolSize"));
    }

    @Test
    public void shouldBuildKafkaEventConsumerConfigGivenEventConsumerConfigView() {

        // Arrange
        final EventConsumerConfigView eventConsumerConfigView = EventConsumerConfigViewTestFactory.buildTestKafkaConsumerConfigView();

        // Act
        final KafkaEventConsumerConfig consumerConfig = (KafkaEventConsumerConfig) abstractEventConsumerConfigViewFactory.convertToEventConsumerConfig(eventConsumerConfigView);

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

    @Test
    public void shouldThrowUnsupportedExceptionGivenNoCompatibleViewFactory() {

        // Arrange
        final StreamViewFactory streamViewFactory = new StreamViewFactory();
        abstractEventConsumerConfigViewFactory = new AbstractEventConsumerConfigViewFactory(new ArrayList<EventConsumerConfigViewFactory>());

        // Act
        try {
            abstractEventConsumerConfigViewFactory.convertToEventConsumerConfig(EventConsumerConfigViewTestFactory.buildTestKafkaConsumerConfigView());
            fail("Should've thrown exception for unknown Config Type");
        } catch (IllegalArgumentException e) {
            assertEquals("ViewFactory not found for Config Type: KAFKA", e.getMessage());
        }
    }
}