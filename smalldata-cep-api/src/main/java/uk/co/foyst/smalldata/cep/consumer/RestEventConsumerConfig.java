package uk.co.foyst.smalldata.cep.consumer;

import uk.co.foyst.smalldata.cep.Stream;

public class RestEventConsumerConfig extends EventConsumerConfig {

    public RestEventConsumerConfig(final EventConsumerId eventConsumerId, final Stream inputStream) {

        super(eventConsumerId, inputStream);
    }
}
