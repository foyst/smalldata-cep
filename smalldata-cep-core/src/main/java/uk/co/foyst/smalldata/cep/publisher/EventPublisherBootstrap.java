package uk.co.foyst.smalldata.cep.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.service.EventPublisherService;

@Component
public class EventPublisherBootstrap implements ApplicationListener<ApplicationContextEvent> {

    private final Logger log = LoggerFactory.getLogger(EventPublisherBootstrap.class);
    private final EventPublisherService eventPublisherService;

    @Autowired
    public EventPublisherBootstrap(EventPublisherService eventPublisherService) {
        this.eventPublisherService = eventPublisherService;
    }

    /**
     * Event Publisher Bootstrapper that attaches a LoggingEventPublisher, so at the very least all CEP Events
     * can be logged via a configurable logger
     *
     * @param applicationContextEvent
     */
    @Override
    public void onApplicationEvent(ApplicationContextEvent applicationContextEvent) {

        if (applicationContextEvent instanceof ContextStartedEvent) {

            log.info("Bootstrapping LoggingEventPublisher");
            final LoggingEventPublisher loggingEventPublisher = new LoggingEventPublisher();
            eventPublisherService.registerEventPublisher(loggingEventPublisher);
        }
    }
}
