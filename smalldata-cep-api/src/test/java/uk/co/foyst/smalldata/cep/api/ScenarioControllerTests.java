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
import uk.co.foyst.smalldata.cep.Scenario;
import uk.co.foyst.smalldata.cep.ScenarioId;
import uk.co.foyst.smalldata.cep.StreamId;
import uk.co.foyst.smalldata.cep.api.controller.ScenarioController;
import uk.co.foyst.smalldata.cep.api.dto.ScenarioDto;
import uk.co.foyst.smalldata.cep.api.dto.ScenarioDtoFactory;
import uk.co.foyst.smalldata.cep.service.ScenarioService;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ScenarioControllerTests {

    private static final String API_LOCATION = "http://localhost/v1/scenarios";

    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @Mock
    private ScenarioDtoFactory scenarioDtoFactory;

    @Mock
    private ScenarioService scenarioService;

    @InjectMocks
    private ScenarioController scenarioController;

    @Before
    public void setUp() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(scenarioController).build();
    }

    @Test
    public void shouldGenerateScenarioIdGivenNewScenario() throws Exception {

        // Arrange
        final ScenarioDto scenarioDto = new ScenarioDto();
        final String scenarioDtoString = objectMapper.writeValueAsString(scenarioDto);

        when(scenarioService.create(Mockito.<Scenario>any())).thenReturn(new Scenario(new ScenarioId(), "", "", "", null, null));

        // Act
        mockMvc.perform(post(API_LOCATION).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(scenarioDtoString)).andExpect(status().isCreated());

        // Assert
        final ArgumentCaptor<ScenarioDto> argumentCaptor = ArgumentCaptor.forClass(ScenarioDto.class);
        verify(scenarioDtoFactory, times(1)).convertToScenario(argumentCaptor.capture());

        final ScenarioDto actualScenarioDto = argumentCaptor.getValue();
        assertNotNull("ScenarioId wasn't generated for new Scenario", actualScenarioDto.getScenarioId());
        StreamId.fromString(actualScenarioDto.getScenarioId()); // Check String value can be parsed into a ScenarioId
    }
}