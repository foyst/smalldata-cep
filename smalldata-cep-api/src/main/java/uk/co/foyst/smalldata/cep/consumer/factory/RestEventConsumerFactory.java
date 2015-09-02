package uk.co.foyst.smalldata.cep.consumer.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.adapter.CEPAdapter;
import uk.co.foyst.smalldata.cep.consumer.*;

@Component
public class RestEventConsumerFactory implements EventConsumerFactory {

    private final CEPAdapter cepAdapter;
    private final RestEventConsumerController restEventConsumerController;

    @Autowired
    public RestEventConsumerFactory(final CEPAdapter cepAdapter, final RestEventConsumerController restEventConsumerController) {
        this.cepAdapter = cepAdapter;
        this.restEventConsumerController = restEventConsumerController;
    }

    @Override
    public EventConsumer build(final EventConsumerConfig eventConsumerConfig) {

        EventConsumer eventConsumer = null;
        if (eventConsumerConfig instanceof RestEventConsumerConfig) {

            final RestEventConsumerConfig consumerConfig = (RestEventConsumerConfig) eventConsumerConfig;
            eventConsumer = new RestEventConsumer(consumerConfig, cepAdapter, restEventConsumerController);
        }

        return eventConsumer;
    }
}
