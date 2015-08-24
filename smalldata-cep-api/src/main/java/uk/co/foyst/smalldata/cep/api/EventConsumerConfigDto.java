package uk.co.foyst.smalldata.cep.api;

import java.io.Serializable;
import java.util.Map;

public class EventConsumerConfigDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String eventConsumerId;
    private final String streamId;
    private final String consumerType;
    private final Map<String, String> configProperties;

    public EventConsumerConfigDto(final String eventConsumerId, final String streamId, final String consumerType, final Map<String, String> configProperties) {
        this.eventConsumerId = eventConsumerId;
        this.streamId = streamId;
        this.consumerType = consumerType;
        this.configProperties = configProperties;
    }

    public String getEventConsumerId() {
        return eventConsumerId;
    }

    public String getStreamId() {
        return streamId;
    }

    public String getConsumerType() {
        return consumerType;
    }

    public Map<String, String> getConfigProperties() {
        return configProperties;
    }

}
