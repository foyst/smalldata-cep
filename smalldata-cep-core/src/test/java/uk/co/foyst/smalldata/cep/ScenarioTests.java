package uk.co.foyst.smalldata.cep;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ScenarioTests {

    @Test
    public void scenariosShouldEqualBasedOnScenarioId() {

        // Arrange
        final ScenarioId sharedScenarioId = ScenarioId.fromString("12b1c57e-baa0-49aa-bf4b-7ba86db5019c");
        final Scenario scenario1 = new Scenario(sharedScenarioId, "Scenario1", null, null, null, null);
        final Scenario scenario2 = new Scenario(sharedScenarioId, "Scenario2", null, null, null, null);

        // Assert
        assertTrue(scenario1.equals(scenario2));
    }
}