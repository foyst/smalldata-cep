package uk.co.foyst.smalldata.cep.consumer.transformer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnescapedStringArrayInboundEventTransformerTests {

    final InboundEventTransformer eventTransformer = new UnescapedStringArrayInboundEventTransformer();

    @Test
    public void shouldTransformGivenStringAndIntegerArrayValues() {

        // Arrange
        final String stringArray = "[Middlesbrough Vs Hull,250000.00]";
        final byte[] stringArrayBytes = stringArray.getBytes();

        // Act
        final Object[] actualArray = eventTransformer.convertToObjectArray(stringArrayBytes);

        // Assert
        assertEquals("Middlesbrough Vs Hull", actualArray[0]);
        assertEquals(250000.00d, actualArray[1]);
    }
}