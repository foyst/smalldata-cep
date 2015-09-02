package uk.co.foyst.smalldata.cep.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.service.EventPublisherService;

//TODO: Maybe look at swapping out for an InitializingBean, so can declare them dynamically instead of hard-wiring into ApplicationListener
@Component
public class EventPublisherBootstrap implements ApplicationListener<ApplicationContextEvent> {

    private final LoggingEventPublisher loggingEventPublisher;
    private final Logger log = LoggerFactory.getLogger(EventPublisherBootstrap.class);
    private final EventPublisherService eventPublisherService;

    @Autowired
    public EventPublisherBootstrap(final EventPublisherService eventPublisherService, final LoggingEventPublisher loggingEventPublisher) {
        this.eventPublisherService = eventPublisherService;
        this.loggingEventPublisher = loggingEventPublisher;
    }

    /**
     * Event Publisher Bootstrapper that attaches a LoggingEventPublisher, so at the very least all CEP Events
     * can be logged via a configurable logger
     *
     * @param applicationContextEvent
     */
    @Override
    public void onApplicationEvent(ApplicationContextEvent applicationContextEvent) {

        if (applicationContextEvent instanceof ContextRefreshedEvent) {

            log.info("Bootstrapping LoggingEventPublisher");
            eventPublisherService.registerEventPublisher(loggingEventPublisher);
        }
    }
}
