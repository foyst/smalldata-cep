package uk.co.foyst.smalldata.cep.api;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventConsumerConfigDtoTypeIdResolverTests {

    private final EventConsumerConfigDtoTypeIdResolver idResolver = new EventConsumerConfigDtoTypeIdResolver();

    @Before
    public void setUp() {

        final JavaType javaType = TypeFactory.defaultInstance().constructType(EventConsumerConfigDto.class, EventConsumerConfigDtoTypeIdResolverTests.class);
        idResolver.init(javaType);
    }

    @Test
    public void shouldReturnJavaTypeForDtoClassGivenSimpleNameAsId() {

        // Act
        final JavaType testConsumerJavaType = idResolver.typeFromId("TestConsumer");

        // Assert
        assertEquals(TestConsumer.class, testConsumerJavaType.getRawClass());
    }

    @Test
    public void shouldReturnSimpleClassNameGivenDtoClass() {

        // Arrange
        final String streamIdString = "12b1c57e-baa0-49aa-bf4b-7ba86db5019c";
        final String eventConsumerIdString = "e8674759-f789-44ec-a42d-261405980ced";
        final EventConsumerConfigDto testConsumer = new TestConsumer(eventConsumerIdString, streamIdString);

        // Act
        final String actualTypeName = idResolver.idFromValue(testConsumer);

        // Assert
        final String expectedTypeName = "TestConsumer";
        assertEquals(expectedTypeName, actualTypeName);
    }

    private class TestConsumer extends EventConsumerConfigDto {

        public TestConsumer(String eventConsumerId, String streamId) {
            super(eventConsumerId, streamId);
        }
    }
}