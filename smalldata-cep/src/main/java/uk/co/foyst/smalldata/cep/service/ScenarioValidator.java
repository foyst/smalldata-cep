package uk.co.foyst.smalldata.cep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.Scenario;
import uk.co.foyst.smalldata.cep.dao.ScenarioView;
import uk.co.foyst.smalldata.cep.dao.ScenarioViewDao;

import java.util.List;

@Component
public class ScenarioValidator {

    private final ScenarioViewDao scenarioViewDao;

    @Autowired
    public ScenarioValidator(ScenarioViewDao scenarioViewDao) {
        this.scenarioViewDao = scenarioViewDao;
    }

    public void verifyScenarioNameIsUnique(final Scenario scenario) throws Exception {

        List<ScenarioView> scenarios = null;

        try {
            scenarios = scenarioViewDao.findAll();
        } catch (final Exception e) {
            // Nothing to do
        }
        if (scenarios != null) {

            final String currentId = scenario.getScenarioId().toString();
            final String currentScenarioName = scenario.getName();
            for (final ScenarioView s : scenarios) {
                final boolean namesMatch = s.getName().equalsIgnoreCase(currentScenarioName);
                final String existingId = s.getScenarioId();
                final boolean scenarioIdsMatch = existingId.equalsIgnoreCase(currentId);
                if (namesMatch && !scenarioIdsMatch) {
                    throw new IllegalArgumentException("A scenario with the same name '" + scenario.getName() + "' already exists.");
                }
            }
        }
    }
}
