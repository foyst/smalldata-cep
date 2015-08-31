package uk.co.foyst.smalldata.cep.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.foyst.smalldata.cep.consumer.EventConsumer;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;
import uk.co.foyst.smalldata.cep.consumer.factory.EventConsumerFactory;
import uk.co.foyst.smalldata.cep.stub.StubEventConsumer;
import uk.co.foyst.smalldata.cep.stub.StubEventConsumerConfig;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EventConsumerManagerTests {

    @Mock
    private EventConsumerFactory eventConsumerFactory;

    private EventConsumerManager eventConsumerManager;

    @Before
    public void setUp() {

        eventConsumerManager = new EventConsumerManager(Arrays.asList(eventConsumerFactory));
    }

    @Test
    public void shouldRegisterAndStartGivenEventConsumerConfig() {

        // Arrange
        final String eventConsumerIdString = "c58d6063-7501-4584-b62a-763114055e05";
        final EventConsumerId eventConsumerId = EventConsumerId.fromString(eventConsumerIdString);

        final EventConsumerConfig eventConsumerConfig = mock(EventConsumerConfig.class);
        when(eventConsumerConfig.getEventConsumerId()).thenReturn(eventConsumerId);
        final EventConsumer eventConsumer = mock(EventConsumer.class);
        when(eventConsumer.isStarted()).thenReturn(true);

        when(eventConsumerFactory.build(eventConsumerConfig)).thenReturn(eventConsumer);

        // Act
        eventConsumerManager.registerAndStart(eventConsumerConfig);

        // Assert
        verify(eventConsumer, times(1)).start();
        assertThat(eventConsumerManager.isStarted(eventConsumerId), is(equalTo(true)));
    }

    @Test
    public void shouldStopAndUnregisterEventConsumerGivenId() {

        // Arrange
        final String eventConsumerIdString = "c58d6063-7501-4584-b62a-763114055e05";
        final EventConsumerId eventConsumerId = EventConsumerId.fromString(eventConsumerIdString);

        final EventConsumerConfig eventConsumerConfig = mock(EventConsumerConfig.class);
        when(eventConsumerConfig.getEventConsumerId()).thenReturn(eventConsumerId);
        final EventConsumer eventConsumer = mock(EventConsumer.class);

        when(eventConsumerFactory.build(eventConsumerConfig)).thenReturn(eventConsumer);

        eventConsumerManager.registerAndStart(eventConsumerConfig);

        // Act
        eventConsumerManager.stopAndUnregister(eventConsumerId);

        // Assert
        verify(eventConsumer, times(1)).stop();
        assertThat(eventConsumerManager.isStarted(eventConsumerId), is(equalTo(false)));
    }

    @Test
    public void shouldReturnNotStartedGivenUnregisteredConsumer() {

        // Arrange
        final String eventConsumerIdString = "c58d6063-7501-4584-b62a-763114055e05";
        final EventConsumerId eventConsumerId = EventConsumerId.fromString(eventConsumerIdString);

        // Act
        final boolean consumerIsStarted = eventConsumerManager.isStarted(eventConsumerId);

        // Assert
        assertFalse(consumerIsStarted);
    }

    @Test
    public void shouldBuildEventConsumerGivenCompatibleFactory() {

        // Arrange
        final String eventConsumerIdString = "c58d6063-7501-4584-b62a-763114055e05";
        final EventConsumerId eventConsumerId = EventConsumerId.fromString(eventConsumerIdString);

        final EventConsumerConfig eventConsumerConfig = new StubEventConsumerConfig(eventConsumerId);
        final EventConsumer eventConsumer = new StubEventConsumer();
        when(eventConsumerFactory.build(eventConsumerConfig)).thenReturn(eventConsumer);

        // Act
        eventConsumerManager.registerAndStart(eventConsumerConfig);

        // Assert
        assertTrue(eventConsumerManager.isStarted(eventConsumerId));
    }

    @Test
    public void shouldThrowExceptionGivenNoCompatibleFactoriesForEventConsumerConfig() {

        // Arrange
        final EventConsumerConfig eventConsumerConfig = new StubEventConsumerConfig();
        when(eventConsumerFactory.build(eventConsumerConfig)).thenReturn(null);

        // Act
        try {
            eventConsumerManager.registerAndStart(eventConsumerConfig);
            fail("Should throw IllegalArgumentException if no factories available");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("No compatible builder for config of type StubEventConsumerConfig", e.getMessage());
        }
    }
}