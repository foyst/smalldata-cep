package uk.co.foyst.smalldata.cep.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;
import uk.co.foyst.smalldata.cep.service.EventConsumerService;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class EventConsumerControllerTests {

    private static final String API_LOCATION = "http://localhost/v1/eventConsumers";

    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @Mock
    private AbstractEventConsumerConfigDtoFactory configDtoFactory;

    @Mock
    private EventConsumerService eventConsumerService;

    @InjectMocks
    private EventConsumerController eventConsumerController;

    @Before
    public void setUp() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(eventConsumerController).build();
    }

    @Test
    public void shouldGenerateEventConsumerIdGivenNewEventConsumer() throws Exception {

        // Arrange
        final EventConsumerConfigDto configDto = new KafkaEventConsumerConfigDto();
        final String configDtoString = objectMapper.writeValueAsString(configDto);

        when(eventConsumerService.create(Mockito.<EventConsumerConfig>any())).thenReturn(mock(EventConsumerConfig.class));

        // Act
        mockMvc.perform(post(API_LOCATION).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(configDtoString)).andExpect(status().isCreated());

        // Assert
        final ArgumentCaptor<EventConsumerConfigDto> argumentCaptor = ArgumentCaptor.forClass(EventConsumerConfigDto.class);
        verify(configDtoFactory, times(1)).convertToEventConsumerConfig(argumentCaptor.capture());

        final EventConsumerConfigDto actualConfigDto = argumentCaptor.getValue();
        assertNotNull("EventConsumerId wasn't generated for new EventConsumer", actualConfigDto.getEventConsumerId());
        EventConsumerId.fromString(actualConfigDto.getEventConsumerId()); // Check String value can be parsed into a ScenarioId
    }
}