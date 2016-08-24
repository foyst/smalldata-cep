package uk.co.foyst.smalldata.cep.publisher.ws;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import uk.co.foyst.smalldata.cep.CEPEvent;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WebSocketEventPublisherTests {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private WebSocketEventPublisher webSocketEventPublisher;

    @Test
    public void shouldStringifyCepEventWhenPassingToClient() {

        // Arrange
        CEPEvent event = new CEPEvent("TestStream", 12345, new Object[]{"John", true, 321});
        CEPEvent[] events = new CEPEvent[]{event};

        // Act
        webSocketEventPublisher.receive(events);

        // Assert
        final ArgumentCaptor<Object> objectArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        verify(messagingTemplate, times(1)).convertAndSend(eq("/v1/topic"), objectArgumentCaptor.capture());
        final String actualPayload = (String) objectArgumentCaptor.getValue();
        final String expectedPayload = "{\"timestamp\": \"1970-01-01 00:00:12,345\", \"streamId\": \"TestStream\", \"eventData\": \"[John, true, 321]\"}";
        assertEquals(expectedPayload, actualPayload);
    }
}