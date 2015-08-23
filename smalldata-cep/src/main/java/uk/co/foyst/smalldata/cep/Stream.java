package uk.co.foyst.smalldata.cep;

public class Stream {

    private StreamId streamId;
    private String name;
    private String definition;
    private String description;

    public Stream(final StreamId streamId, final String name, final String definition,
                  final String description) {
        this.streamId = streamId;
        this.name = name;
        this.definition = definition;
        this.description = description;
    }

    public StreamId getStreamId() {
        return streamId;
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
}
