package uk.co.foyst.smalldata.cep.dao;

import org.junit.Before;
import org.junit.Test;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;
import uk.co.foyst.smalldata.cep.consumer.RestEventConsumerConfig;
import uk.co.foyst.smalldata.cep.testfactory.EventConsumerConfigViewTestFactory;
import uk.co.foyst.smalldata.cep.testfactory.StreamTestFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class RestEventConsumerConfigViewFactoryTests {

    private StreamViewFactory streamViewFactory;
    private RestEventConsumerConfigViewFactory viewFactory;

    @Before
    public void setUp() {

        streamViewFactory = new StreamViewFactory();
        viewFactory = new RestEventConsumerConfigViewFactory(streamViewFactory);
    }

    @Test
    public void shouldBuildConfigViewGivenRestEventConsumerConfig() {

        // Arrange
        final EventConsumerId eventConsumerId = new EventConsumerId();
        final Stream inputStream = StreamTestFactory.buildTestInputStream();
        final EventConsumerConfig restEventConsumerConfig = new RestEventConsumerConfig(eventConsumerId, inputStream);

        // Act
        final EventConsumerConfigView eventConsumerConfigView = viewFactory.build(restEventConsumerConfig);

        // Assert
        assertEquals(eventConsumerId.toString(), eventConsumerConfigView.getEventConsumerId());
        assertEquals(inputStream.getStreamId().toString(), eventConsumerConfigView.getStreamView().getStreamId());
        assertEquals("REST", eventConsumerConfigView.getConsumerType());
    }

    @Test
    public void shouldReturnNullGivenIncompatibleEventConsumerConfigType() {

        // Arrange
        final EventConsumerConfig eventConsumerConfig = mock(EventConsumerConfig.class);

        // Act
        final EventConsumerConfigView eventConsumerConfigView = viewFactory.build(eventConsumerConfig);

        // Assert
        assertThat(eventConsumerConfigView, is(nullValue()));
    }

    @Test
    public void shouldBuildRestEventConsumerConfigGivenEventConsumerConfigView() {

        // Arrange
        final EventConsumerConfigView eventConsumerConfigView = EventConsumerConfigViewTestFactory.buildTestRestConsumerConfigView();

        // Act
        final RestEventConsumerConfig consumerConfig = (RestEventConsumerConfig) viewFactory.convertToEventConsumerConfig(eventConsumerConfigView);

        // Assert
        assertEquals(eventConsumerConfigView.getEventConsumerId(), consumerConfig.getEventConsumerId().toString());
        assertEquals(eventConsumerConfigView.getStreamView().getStreamId(), consumerConfig.getInputStream().getStreamId().toString());
        assertEquals(eventConsumerConfigView.getStreamView().getName(), consumerConfig.getInputStream().getName());
        assertEquals(eventConsumerConfigView.getStreamView().getDescription(), consumerConfig.getInputStream().getDescription());
        assertEquals(eventConsumerConfigView.getStreamView().getDefinition(), consumerConfig.getInputStream().getDefinition());
    }
}