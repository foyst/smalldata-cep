package uk.co.foyst.smalldata.cep.consumer.transformer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;

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

    @Test
    public void shouldHandleNestedFieldsByFlattening() {

        // Arrange
        final String pointScoredMessage = "{\"source\":\"Feed\",\"server\":\"HOME\",\"eventId\":\"c4d45f8a-4e91-34b3-891a-1870912a7332\",\"version\":0.24,\"eventElementType\":\"PointScored\"," +
                "\"pointType\":\"None\",\"metadata\":{\"createdService\":\"i-man\",\"createdDate\":\"2016-08-19T04:45:07.920\"},\"correlationId\":\"113ca229-b020-48be-a5f6-fa572c23dd00\"," +
                "\"scoredBy\":\"AWAY\",\"sequenceNumber\":263}";
        final byte[] pointScoredMessageBytes = pointScoredMessage.getBytes();

        final Object[] response = eventTransformer.convertToObjectArray(pointScoredMessageBytes);

        assertThat(response, arrayWithSize(11));
        assertEquals("i-man", response[6]);
        assertEquals("2016-08-19T04:45:07.920", response[7]);
    }
}