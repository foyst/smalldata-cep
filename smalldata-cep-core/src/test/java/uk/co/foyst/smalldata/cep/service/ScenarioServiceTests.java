package uk.co.foyst.smalldata.cep.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.foyst.smalldata.cep.Scenario;
import uk.co.foyst.smalldata.cep.ScenarioId;
import uk.co.foyst.smalldata.cep.adapter.CEPAdapter;
import uk.co.foyst.smalldata.cep.dao.ScenarioView;
import uk.co.foyst.smalldata.cep.dao.ScenarioViewDao;
import uk.co.foyst.smalldata.cep.dao.ScenarioViewFactory;
import uk.co.foyst.smalldata.cep.exception.EntityNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScenarioServiceTests {

    @Mock
    private ScenarioViewDao scenarioViewDao;

    @Spy
    private ScenarioViewFactory scenarioViewFactory;

    @Mock
    private CEPAdapter cepAdapter;

    @InjectMocks
    private ScenarioService scenarioService;

    @Test
    public void shouldThrowExceptionWhenDeletingScenarioGivenUnknownId() throws Exception {

        // Arrange
        final String scenarioIdString = "12b1c57e-baa0-49aa-bf4b-7ba86db5019c";
        ScenarioId scenarioId = ScenarioId.fromString(scenarioIdString);
        when(scenarioViewDao.findOne(scenarioIdString)).thenReturn(null);

        // Act
        try {
            scenarioService.delete(scenarioId);
            fail("Should've thrown EntityNotFoundException when attempting to delete unknown Scenario");
        } catch (EntityNotFoundException e) {
            // Assert
            assertEquals("Scenario with Id '" + scenarioIdString + "' does not exist.", e.getMessage());
        }
    }

    @Test
    public void shouldRemoveScenarioFromCepWhenDeleting() throws Exception {

        // Arrange
        final String scenarioIdString = "12b1c57e-baa0-49aa-bf4b-7ba86db5019c";
        final ScenarioId scenarioId = ScenarioId.fromString(scenarioIdString);
        final ScenarioView deletingScenarioView = new ScenarioView(scenarioIdString, null, null, null, null, null);
        final Scenario deletingScenario = new Scenario(scenarioId, null, null, null, null, null);
        when(scenarioViewDao.findOne(scenarioIdString)).thenReturn(deletingScenarioView);

        // Act
        scenarioService.delete(scenarioId);

        // Assert
        verify(cepAdapter).remove(deletingScenario);
    }

    @Test
    public void shouldDeleteScenarioFromDaoWhenDeleting() throws Exception {

        // Arrange
        final String scenarioIdString = "12b1c57e-baa0-49aa-bf4b-7ba86db5019c";
        final ScenarioId scenarioId = ScenarioId.fromString(scenarioIdString);
        final ScenarioView deletingScenarioView = new ScenarioView(scenarioIdString, null, null, null, null, null);
        when(scenarioViewDao.findOne(scenarioIdString)).thenReturn(deletingScenarioView);

        // Act
        scenarioService.delete(scenarioId);

        // Assert
        verify(scenarioViewDao).delete(deletingScenarioView);
    }

}