package uk.co.foyst.smalldata.cep.api.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "consumerType")
@JsonTypeIdResolver(EventConsumerConfigDtoTypeIdResolver.class)
public abstract class EventConsumerConfigDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String eventConsumerId;
    private String streamId;

    public EventConsumerConfigDto() {}

    public EventConsumerConfigDto(final String eventConsumerId, final String streamId) {
        this.eventConsumerId = eventConsumerId;
        this.streamId = streamId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEventConsumerId() {
        return eventConsumerId;
    }

    public void setEventConsumerId(String eventConsumerId) {
        this.eventConsumerId = eventConsumerId;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }
}
