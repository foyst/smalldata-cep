package uk.co.foyst.smalldata.cep.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class RestEventConsumerControllerTests {

    private static final String API_LOCATION = "http://localhost/v1/restEventConsumer";

    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @InjectMocks
    private RestEventConsumerController eventConsumerController;

    @Before
    public void setUp() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(eventConsumerController).build();
    }

    @Test
    public void shouldPassEventToRestEventConsumer() throws Exception {

        // Arrange
        final Object[] eventAttributes = new Object[]{1234, 1, true, "SomeText"};
        final String event = objectMapper.writeValueAsString(eventAttributes);
        final String streamName = "testStream";

        final RestEventConsumer restEventConsumer = mock(RestEventConsumer.class);
        when(restEventConsumer.getInputStreamName()).thenReturn(streamName);
        eventConsumerController.registerEventConsumer(restEventConsumer);

        // Act
        mockMvc.perform(post(API_LOCATION + "/" + streamName).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(event)).andExpect(status().isOk());

        // Assert
        verify(restEventConsumer, times(1)).sendEvent(eventAttributes);
    }

}