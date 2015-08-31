package uk.co.foyst.smalldata.cep.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EventConsumerServiceTests {

    @Mock
    private EventConsumerManager eventConsumerManager;

    @Mock
    private EventConsumerConfigService eventConsumerConfigService;

    @InjectMocks
    private EventConsumerService eventConsumerService;

    @Test
    public void shouldStartEventConsumerGivenEventConsumerId() {

        // Arrange
        final String eventConsumerIdString = "c58d6063-7501-4584-b62a-763114055e05";
        final EventConsumerId eventConsumerId = EventConsumerId.fromString(eventConsumerIdString);
        final EventConsumerConfig eventConsumerConfig = mock(EventConsumerConfig.class);
        when(eventConsumerConfigService.read(eventConsumerId)).thenReturn(eventConsumerConfig);

        // Act
        eventConsumerService.startEventConsumer(eventConsumerId);

        // Assert
        verify(eventConsumerConfigService, times(1)).read(eventConsumerId);
        verify(eventConsumerManager, times(1)).registerAndStart(eventConsumerConfig);
    }

    @Test
    public void shouldStopEventConsumerGivenEventConsumerId() {

        // Arrange
        final String eventConsumerIdString = "c58d6063-7501-4584-b62a-763114055e05";
        final EventConsumerId eventConsumerId = EventConsumerId.fromString(eventConsumerIdString);

        // Act
        eventConsumerService.stopEventConsumer(eventConsumerId);

        // Assert
        verifyZeroInteractions(eventConsumerConfigService);
        verify(eventConsumerManager, times(1)).stopAndUnregister(eventConsumerId);
    }
}