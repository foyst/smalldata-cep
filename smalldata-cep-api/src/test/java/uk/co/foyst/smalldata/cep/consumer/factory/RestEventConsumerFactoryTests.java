package uk.co.foyst.smalldata.cep.consumer.factory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.foyst.smalldata.cep.adapter.CEPAdapter;
import uk.co.foyst.smalldata.cep.consumer.EventConsumer;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.RestEventConsumerController;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class RestEventConsumerFactoryTests {

    @Mock
    private CEPAdapter cepAdapter;

    @Mock
    private RestEventConsumerController restEventConsumerController;

    @InjectMocks
    private RestEventConsumerFactory restEventConsumerFactory;

    @Test
    public void shouldReturnNullGivenIncomptatibleEventConsumerConfig() {

        // Arrange
        final EventConsumerConfig eventConsumerConfig = mock(EventConsumerConfig.class);

        // Act
        final EventConsumer eventConsumer = restEventConsumerFactory.build(eventConsumerConfig);

        // Assert
        assertThat(eventConsumer, is(nullValue()));
    }
}