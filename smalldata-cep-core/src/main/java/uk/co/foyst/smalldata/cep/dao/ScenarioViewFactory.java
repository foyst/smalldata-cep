package uk.co.foyst.smalldata.cep.dao;

import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.Scenario;
import uk.co.foyst.smalldata.cep.ScenarioId;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScenarioViewFactory {

    public ScenarioView build(final Scenario scenario) throws Exception {

        return new ScenarioView(scenario.getScenarioId().toString(), scenario.getName(), scenario.getDefinition(), scenario.getDescription(), scenario.getCreatedAt(), scenario.getUpdatedAt());
    }

    public List<ScenarioView> build(final List<Scenario> entities) throws Exception {

        final List<ScenarioView> scenarioViews = new ArrayList<>();
        for (Scenario scenario : entities) {
            scenarioViews.add(build(scenario));
        }
        return scenarioViews;
    }

    public Scenario convertToDomainObject(final ScenarioView view) throws Exception {

        final ScenarioId scenarioId = ScenarioId.fromString(view.getScenarioId());

        return new Scenario(scenarioId, view.getName(), view.getDefinition(), view.getDescription(), view.getCreatedAt(),
                view.getUpdatedAt());
    }

    public List<Scenario> convertToDomainObject(final List<ScenarioView> views) throws Exception {

        final List<Scenario> scenarios = new ArrayList<>();

        for (final ScenarioView view : views) {
            scenarios.add(convertToDomainObject(view));
        }

        return scenarios;
    }
}
