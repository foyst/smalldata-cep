package uk.co.foyst.smalldata.cep.consumer.factory;

import org.apache.zookeeper.server.quorum.QuorumCnxManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.adapter.CEPAdapter;
import uk.co.foyst.smalldata.cep.consumer.*;
import uk.co.foyst.smalldata.cep.consumer.transformer.InboundEventTransformer;
import uk.co.foyst.smalldata.cep.consumer.transformer.OrderedJsonInboundEventTransformer;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class KafkaEventConsumerFactoryTests {

    @Mock
    private CEPAdapter cepAdapter;

    @InjectMocks
    private KafkaEventConsumerFactory kafkaEventConsumerFactory;

    @Test
    public void shouldReturnNullGivenIncomptatibleEventConsumerConfig() {

        // Arrange
        final EventConsumerConfig eventConsumerConfig = mock(EventConsumerConfig.class);

        // Act
        final EventConsumer eventConsumer = kafkaEventConsumerFactory.build(eventConsumerConfig);

        // Assert
        assertThat(eventConsumer, is(nullValue()));
    }

    @Test
    public void shouldReturnOrderedJsonTransformer() {

        // Arrange
        final MessageTransformer messageTransformer = MessageTransformer.ORDERED_JSON;

        // Act
        final InboundEventTransformer eventTransformer = kafkaEventConsumerFactory.buildEventTransformer(messageTransformer);

        // Assert
        assertThat(eventTransformer, instanceOf(OrderedJsonInboundEventTransformer.class));
    }
}