package uk.co.foyst.smalldata.cep.api;

import org.joda.time.DateTime;

public class ScenarioDto {

    protected String scenarioId;
    protected String name;
    protected String definition;
    protected String description;
    protected DateTime createdAt;
    protected DateTime updatedAt;
    protected String updatedBy;

    public ScenarioDto() {
    }

    public ScenarioDto(String scenarioId, String name, String definition, String description, DateTime createdAt, DateTime updatedAt, String updatedBy) {
        this.scenarioId = scenarioId;
        this.name = name;
        this.definition = definition;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(DateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}