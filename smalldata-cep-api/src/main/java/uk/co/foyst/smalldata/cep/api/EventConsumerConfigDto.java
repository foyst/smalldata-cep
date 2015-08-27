package uk.co.foyst.smalldata.cep.api;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "consumerType")
public abstract class EventConsumerConfigDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String eventConsumerId;
    private final String streamId;

    public EventConsumerConfigDto(final String eventConsumerId, final String streamId) {
        this.eventConsumerId = eventConsumerId;
        this.streamId = streamId;
    }

    public String getEventConsumerId() {
        return eventConsumerId;
    }

    public String getStreamId() {
        return streamId;
    }
}
