package uk.co.foyst.smalldata.cep.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;
import uk.co.foyst.smalldata.cep.consumer.RestEventConsumerConfig;

import java.util.Collections;

@Component
public class RestEventConsumerConfigViewFactory extends EventConsumerConfigViewFactory {

    private static final String REST_CONSUMER_TYPE = "REST";

    @Autowired
    public RestEventConsumerConfigViewFactory(StreamViewFactory streamViewFactory) {
        super(streamViewFactory);
    }

    @Override
    public EventConsumerConfigView build(final EventConsumerConfig eventConsumerConfig) {

        EventConsumerConfigView eventConsumerConfigView = null;
        if(eventConsumerConfig instanceof RestEventConsumerConfig) {
            final StreamView streamView = streamViewFactory.build(eventConsumerConfig.getInputStream());

            eventConsumerConfigView = new EventConsumerConfigView(eventConsumerConfig.getEventConsumerId().toString(), streamView, REST_CONSUMER_TYPE, Collections.<String, String>emptyMap());
        }

        return eventConsumerConfigView;
    }

    @Override
    public EventConsumerConfig convertToEventConsumerConfig(EventConsumerConfigView eventConsumerConfigView) {

        EventConsumerConfig eventConsumerConfig = null;
        if (eventConsumerConfigView.getConsumerType().equals(REST_CONSUMER_TYPE)) {
            EventConsumerId eventConsumerId = EventConsumerId.fromString(eventConsumerConfigView.getEventConsumerId());
            Stream inputStream = streamViewFactory.convertToStream(eventConsumerConfigView.getStreamView());
            eventConsumerConfig = new RestEventConsumerConfig(eventConsumerId, inputStream);
        }
        return eventConsumerConfig;
    }
}