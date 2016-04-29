package uk.co.foyst.smalldata.cep.consumer.transformer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderedJsonInboundEventTransformerTests {

    final InboundEventTransformer eventTransformer = new OrderedJsonInboundEventTransformer();

    @Test
    public void shouldTransformGivenSimpleJsonObject() {

        // Arrange
        final String jsonString = "{\"make\": \"Ford\", \"model\": \"Focus\", \"year\": 2014}";
        final byte[] jsonStringBytes = jsonString.getBytes();

        // Act
        final Object[] response = eventTransformer.convertToObjectArray(jsonStringBytes);

        // Assert
        assertEquals("Ford", response[0]);
        assertEquals("Focus", response[1]);
        assertEquals(2014, response[2]);
    }

    @Test
    public void shouldTransformDecimalValues() {

        // Arrange
        final String jsonString = "{\"longitude\": 23.55556}";
        final byte[] jsonStringBytes = jsonString.getBytes();

        // Act
        final Object[] response = eventTransformer.convertToObjectArray(jsonStringBytes);

        // Assert
        assertEquals(23.55556d, response[0]);
    }

    @Test
    public void shouldHandleNullValues() {

        // Arrange
        final String jsonString = "{\"longitude\": null}";
        final byte[] jsonStringBytes = jsonString.getBytes();

        // Act
        final Object[] response = eventTransformer.convertToObjectArray(jsonStringBytes);

        // Assert
        assertEquals(null, response[0]);
    }
}