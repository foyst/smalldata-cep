package uk.co.foyst.smalldata.cep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;

import java.util.List;

@Service
public class EventConsumerService {

    private final EventConsumerConfigService eventConsumerConfigService;
    private final EventConsumerManager eventConsumerManager;

    @Autowired
    public EventConsumerService(EventConsumerConfigService eventConsumerConfigService, EventConsumerManager eventConsumerManager) {
        this.eventConsumerConfigService = eventConsumerConfigService;
        this.eventConsumerManager = eventConsumerManager;
    }

    public List<EventConsumerConfig> readAll() {
        return eventConsumerConfigService.readAll();
    }

    public EventConsumerConfig read(final EventConsumerId eventConsumerId) {
        return eventConsumerConfigService.read(eventConsumerId);
    }

    public EventConsumerConfig create(EventConsumerConfig eventConsumerConfig) {
        return eventConsumerConfigService.create(eventConsumerConfig);
    }

    public EventConsumerConfig update(EventConsumerConfig eventConsumerConfig) {
        return eventConsumerConfigService.update(eventConsumerConfig);
    }

    public void startEventConsumer(final EventConsumerId eventConsumerId) {

        final EventConsumerConfig eventConsumerConfig = eventConsumerConfigService.read(eventConsumerId);
        eventConsumerManager.registerAndStart(eventConsumerConfig);
    }

    public void stopEventConsumer(final EventConsumerId eventConsumerId) {

        eventConsumerManager.stopAndUnregister(eventConsumerId);
    }
}
