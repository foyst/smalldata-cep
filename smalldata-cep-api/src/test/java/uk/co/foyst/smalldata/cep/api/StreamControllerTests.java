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
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.StreamId;
import uk.co.foyst.smalldata.cep.service.StreamService;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class StreamControllerTests {

    private static final String API_LOCATION = "http://localhost/v1/streams";

    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @Mock
    private StreamDtoFactory streamDtoFactory;

    @Mock
    private StreamService streamService;

    @InjectMocks
    private StreamController streamController;

    @Before
    public void setUp() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(streamController).build();
    }

    @Test
    public void shouldGenerateStreamIdGivenNewStream() throws Exception {

        // Arrange
        final StreamDto streamDto = new StreamDto(null, "Test Stream", "define stream TestStream(severity long);", "Test Stream Description");
        final String streamDtoString = objectMapper.writeValueAsString(streamDto);

        when(streamService.add(Mockito.<Stream>any())).thenReturn(new Stream(new StreamId(), "", "", ""));

        // Act
        mockMvc.perform(post(API_LOCATION).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(streamDtoString)).andExpect(status().isCreated());

        // Assert
        final ArgumentCaptor<StreamDto> argumentCaptor = ArgumentCaptor.forClass(StreamDto.class);
        verify(streamDtoFactory, times(1)).convertToStream(argumentCaptor.capture());

        final StreamDto actualStreamDto = argumentCaptor.getValue();
        assertNotNull(actualStreamDto.getStreamId());
        StreamId.fromString(actualStreamDto.getStreamId()); // Check String value can be parsed into a StreamId
    }
}