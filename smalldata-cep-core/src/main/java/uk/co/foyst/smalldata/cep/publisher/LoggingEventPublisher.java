package uk.co.foyst.smalldata.cep.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.CEPEvent;

@Component
public class LoggingEventPublisher extends EventPublisher {

    private final Logger log = LoggerFactory.getLogger(LoggingEventPublisher.class);

    @Override
    public void receive(final CEPEvent[] events) {

        for (final CEPEvent cepEvent : events)
            log.debug("{} -> {} -> {}", cepEvent.getTimeStamp(), cepEvent.getStreamId(), cepEvent.getData());
    }
}
