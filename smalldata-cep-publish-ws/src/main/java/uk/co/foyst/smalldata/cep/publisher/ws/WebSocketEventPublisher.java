package uk.co.foyst.smalldata.cep.publisher.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.CEPEvent;
import uk.co.foyst.smalldata.cep.publisher.EventPublisher;

@Component
public class WebSocketEventPublisher extends EventPublisher {

    private SimpMessagingTemplate template;

    @Autowired
    public WebSocketEventPublisher(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void receive(CEPEvent[] events) {

        for (final CEPEvent cepEvent : events)
            template.convertAndSend("/v1/topic", cepEvent.toString());
    }
}
