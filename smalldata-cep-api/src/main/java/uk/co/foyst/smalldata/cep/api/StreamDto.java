package uk.co.foyst.smalldata.cep.api;

public class StreamDto {
    private String streamId;
    private String name;
    private String definition;
    private String description;

    public StreamDto() {
    }

    public StreamDto(String streamId, String name, String definition, String description) {
        this.streamId = streamId;
        this.name = name;
        this.definition = definition;
        this.description = description;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
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
}