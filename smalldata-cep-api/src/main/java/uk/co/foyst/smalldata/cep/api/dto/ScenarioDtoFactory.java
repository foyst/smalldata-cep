package uk.co.foyst.smalldata.cep.api.dto;

import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.Scenario;
import uk.co.foyst.smalldata.cep.ScenarioId;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScenarioDtoFactory {

    public ScenarioDto build(final Scenario scenario) {

        return new ScenarioDto(scenario.getScenarioId().toString(), scenario.getName(), scenario.getDefinition(), scenario.getDescription(),
                scenario.getCreatedAt(), scenario.getUpdatedAt());
    }

    public List<ScenarioDto> build(final List<Scenario> scenarios) {

        final List<ScenarioDto> dtos = new ArrayList<>();

        for (final Scenario scenario : scenarios) {
            dtos.add(build(scenario));
        }

        return dtos;
    }

    public Scenario convertToScenario(final ScenarioDto dto) throws Exception {

        final ScenarioId scenarioId = ScenarioId.fromString(dto.getScenarioId());

        return new Scenario(scenarioId, dto.getName(), dto.getDefinition(), dto.getDescription(), dto.getCreatedAt(),
                dto.getUpdatedAt());
    }

    public List<Scenario> convertToScenario(final List<ScenarioDto> dtos) throws Exception {

        final List<Scenario> scenarios = new ArrayList<>();

        for (final ScenarioDto dto : dtos) {
            scenarios.add(convertToScenario(dto));
        }

        return scenarios;
    }
}
