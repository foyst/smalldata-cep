package uk.co.foyst.smalldata.cep.publisher;

import uk.co.foyst.smalldata.cep.CEPEvent;

public class ConsoleEventPublisher extends EventPublisher {

    public void receive(final CEPEvent[] events) {

        for (final CEPEvent cepEvent : events)
            System.out.println(cepEvent);
    }
}
