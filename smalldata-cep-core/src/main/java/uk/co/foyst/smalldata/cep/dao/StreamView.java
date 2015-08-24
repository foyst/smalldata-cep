package uk.co.foyst.smalldata.cep.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.Serializable;

@Entity
public class StreamView implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    protected String streamId;

    @Column(unique = true, nullable = false)
    protected String name;

    @Lob
    @Column(length = 1200, nullable = false)
    protected String definition;

    protected String description;

    public StreamView() {
    }

    public StreamView(final String streamId, final String name, final String definition, final String description
    ) {

        this.name = name;
        this.streamId = streamId;
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
