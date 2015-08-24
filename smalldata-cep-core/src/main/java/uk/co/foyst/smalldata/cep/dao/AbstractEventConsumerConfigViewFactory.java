package uk.co.foyst.smalldata.cep.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;

import java.util.ArrayList;
import java.util.List;

@Component
public class AbstractEventConsumerConfigViewFactory {

    private final List<EventConsumerConfigViewFactory> eventConsumerConfigViewFactories;

    @Autowired
    public AbstractEventConsumerConfigViewFactory(final List<EventConsumerConfigViewFactory> configViewFactories) {

        this.eventConsumerConfigViewFactories = configViewFactories;
    }

    public EventConsumerConfigView build(final EventConsumerConfig eventConsumerConfig) {

        EventConsumerConfigView eventConsumerConfigView = null;
        for (final EventConsumerConfigViewFactory configViewFactory : eventConsumerConfigViewFactories)
            if (configViewFactory.compatibleWith(eventConsumerConfig)) {
                eventConsumerConfigView = configViewFactory.build(eventConsumerConfig);
                break;
            }

        if (eventConsumerConfig == null)
            throw new IllegalArgumentException("ViewFactory not found for Config Type: " + eventConsumerConfig.getClass().getSimpleName());

        return eventConsumerConfigView;
    }

    public EventConsumerConfig convertToEventConsumerConfig(final EventConsumerConfigView eventConsumerConfigView) {

        EventConsumerConfig eventConsumerConfig = null;
        for (final EventConsumerConfigViewFactory configViewFactory : eventConsumerConfigViewFactories)
            if (configViewFactory.compatibleWith(eventConsumerConfigView)) {
                eventConsumerConfig = configViewFactory.convertToEventConsumerConfig(eventConsumerConfigView);
                break;
            }

        if (eventConsumerConfig == null)
            throw new IllegalArgumentException("ViewFactory not found for Config Type: " + eventConsumerConfigView.getConsumerType());

        return eventConsumerConfig;
    }

    public List<EventConsumerConfig> convertToEventConsumerConfig(final List<EventConsumerConfigView> eventConsumerConfigViews) {

        final List<EventConsumerConfig> eventConsumerConfigs = new ArrayList<>(eventConsumerConfigViews.size());

        for (final EventConsumerConfigView eventConsumerConfigView : eventConsumerConfigViews) {
            eventConsumerConfigs.add(convertToEventConsumerConfig(eventConsumerConfigView));
        }

        return eventConsumerConfigs;
    }
}
