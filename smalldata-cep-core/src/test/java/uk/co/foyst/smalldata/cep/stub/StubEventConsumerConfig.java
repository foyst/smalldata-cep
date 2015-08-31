package uk.co.foyst.smalldata.cep.stub;

import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;

public class StubEventConsumerConfig extends EventConsumerConfig {

    public StubEventConsumerConfig() {
        super(new EventConsumerId(), null);
    }

    public StubEventConsumerConfig(final EventConsumerId eventConsumerId) {
        super(eventConsumerId, null);
    }
}
