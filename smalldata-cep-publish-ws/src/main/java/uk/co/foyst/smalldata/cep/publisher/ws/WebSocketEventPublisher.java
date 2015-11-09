package uk.co.foyst.smalldata.cep.publisher.ws;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.CEPEvent;
import uk.co.foyst.smalldata.cep.publisher.EventPublisher;

import java.util.Arrays;

@Component
public class WebSocketEventPublisher extends EventPublisher {

    private SimpMessagingTemplate template;
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS").withZoneUTC();

    @Autowired
    public WebSocketEventPublisher(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void receive(CEPEvent[] events) {

        for (final CEPEvent cepEvent : events) {
            template.convertAndSend("/v1/topic", String.format("%s - %s: %s", TIMESTAMP_FORMAT.print(new DateTime(cepEvent.getTimeStamp())), cepEvent.getStreamId(), Arrays.toString(cepEvent.getData())));
        }
    }
}
