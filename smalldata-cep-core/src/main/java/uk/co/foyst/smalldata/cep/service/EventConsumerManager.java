package uk.co.foyst.smalldata.cep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.foyst.smalldata.cep.consumer.EventConsumer;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;
import uk.co.foyst.smalldata.cep.consumer.factory.EventConsumerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventConsumerManager {

    private final List<EventConsumerFactory> eventConsumerFactories;
    private final Map<EventConsumerId, EventConsumer> registeredConsumers;

    @Autowired
    public EventConsumerManager(List<EventConsumerFactory> eventConsumerFactories) {
        this.eventConsumerFactories = eventConsumerFactories;
        this.registeredConsumers = new HashMap<>();
    }

    public void registerAndStart(final EventConsumerConfig eventConsumerConfig) {

        EventConsumer eventConsumer = null;
        for (final EventConsumerFactory eventConsumerFactory : eventConsumerFactories) {

            eventConsumer = eventConsumerFactory.build(eventConsumerConfig);
            if (eventConsumer != null)
                break;
        }

        if (eventConsumer == null)
            throw new IllegalArgumentException("No compatible builder for config of type " + eventConsumerConfig.getClass().getSimpleName());

        registeredConsumers.put(eventConsumerConfig.getEventConsumerId(), eventConsumer);
        eventConsumer.start();
    }

    public boolean isStarted(EventConsumerId eventConsumerId) {

        final EventConsumer eventConsumer = registeredConsumers.get(eventConsumerId);
        if (eventConsumer == null)
            return false;
        else
            return eventConsumer.isStarted();
    }

    public void stopAndUnregister(final EventConsumerId eventConsumerId) {

        registeredConsumers.get(eventConsumerId).stop();
        registeredConsumers.remove(eventConsumerId);
    }
}
