package uk.co.foyst.smalldata.cep.runner;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;
import uk.co.foyst.smalldata.cep.consumer.KafkaEventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.MessageTransformer;
import uk.co.foyst.smalldata.cep.service.EventConsumerManager;
import uk.co.foyst.smalldata.cep.service.StreamService;
import uk.co.foyst.smalldata.cep.testfactory.StreamTestFactory;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:cep-context.xml")
@ActiveProfiles("cep.persistent.hsqldb")
public class KafkaConfigurationIntTests {

    @Autowired
    private StreamService streamService;

    @Autowired
    private EventConsumerManager cepEventConsumerManager;

    @Ignore("Ignored until investigated how to run end to end tests with a Kafka Broker")
    @Test
    public void shouldStartKafkaConsumerGivenConfiguration() throws Exception {

        // Arrange
        final Stream testStream = StreamTestFactory.buildTestInputStream();
        streamService.add(testStream);

        final EventConsumerId eventConsumerId = new EventConsumerId();
        EventConsumerConfig kafkaConsumerConfig = new KafkaEventConsumerConfig(eventConsumerId, testStream, "localhost:8080", "foyst.smalldata", "radarEvents", MessageTransformer.ORDERED_JSON);

        // Act
        cepEventConsumerManager.registerAndStart(kafkaConsumerConfig);

        // Assert
        assertTrue(cepEventConsumerManager.isStarted(eventConsumerId));
    }
}
