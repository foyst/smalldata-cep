package uk.co.foyst.smalldata.cep;

import org.joda.time.DateTime;

public class Scenario {

    private ScenarioId scenarioId;
    private String name;
    private String definition;
    private String description;
    private DateTime createdAt;
    private DateTime updatedAt;

    public Scenario(ScenarioId scenarioId, String name, String definition, String description, DateTime createdAt, DateTime updatedAt) {
        this.scenarioId = scenarioId;
        this.name = name;
        this.definition = definition;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ScenarioId getScenarioId() {
        return scenarioId;
    }

    public String getName() {
        return name;
    }

    public String getDefinition() {
        return definition;
    }

    public String getDescription() {
        return description;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Scenario scenario = (Scenario) o;

        if (!scenarioId.equals(scenario.scenarioId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return scenarioId.hashCode();
    }
}
