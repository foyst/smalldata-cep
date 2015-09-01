package uk.co.foyst.smalldata.cep.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.adapter.CEPAdapter;
import uk.co.foyst.smalldata.cep.service.StreamService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class RestEventConsumerControllerTests {

    private static final String API_LOCATION = "http://localhost/v1/restEventConsumer";

    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @Mock
    private CEPAdapter cepAdapter;

    @Mock
    private StreamService streamService;

    @InjectMocks
    private RestEventConsumerController eventConsumerController;

    @Before
    public void setUp() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(eventConsumerController).build();
    }

    @Test
    public void shouldPassEventToCEPAdapter() throws Exception {

        // Arrange
        final Object[] eventAttributes = new Object[]{1234, 1, true, "SomeText"};
        final String event = objectMapper.writeValueAsString(eventAttributes);
        final String streamName = "testStream";

        final Stream stream = mock(Stream.class);
        eventConsumerController.registerEventConsumer(streamName, stream);

        // Act
        mockMvc.perform(post(API_LOCATION + "/" + streamName).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(event)).andExpect(status().isOk());

        // Assert
        verify(cepAdapter, times(1)).sendEvent(stream, eventAttributes);
    }

}