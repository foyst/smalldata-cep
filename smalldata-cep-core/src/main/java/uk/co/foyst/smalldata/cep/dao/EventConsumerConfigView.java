package uk.co.foyst.smalldata.cep.dao;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Entity
public class EventConsumerConfigView implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String eventConsumerId;

    private StreamView streamView;
    // TODO: Make me an enum
    private String consumerType;

    public EventConsumerConfigView(){}

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @CollectionTable(name = "EventConsumerConfigView_Props")
    @MapKeyColumn(name = "propertyName")
    @Column(name = "propertyValue", length = 1000)
    private Map<String, String> configProperties;

    public EventConsumerConfigView(String eventConsumerId, StreamView streamView, String consumerType, Map<String, String> configProperties) {
        this.eventConsumerId = eventConsumerId;
        this.streamView = streamView;
        this.consumerType = consumerType;
        this.configProperties = configProperties;
    }

    public String getEventConsumerId() {
        return eventConsumerId;
    }

    public StreamView getStreamView() {
        return streamView;
    }

    public String getConsumerType() {
        return consumerType;
    }

    public Map<String, String> getConfigProperties() {
        return configProperties;
    }

}
