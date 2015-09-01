package uk.co.foyst.smalldata.cep.consumer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

@Controller
@RequestMapping(value = "/v1/restEventConsumer")
public class RestEventConsumerController implements EventConsumer {

    private final HashMap<String, RestEventConsumer> registeredStreams;

    public RestEventConsumerController() {
        this.registeredStreams = new HashMap<>();
    }

    @Override
    public void start() {}

    @Override
    public void stop() {}

    @Override
    public boolean isStarted() {
        return true;
    }

    @RequestMapping(value = "/{streamName}", method = RequestMethod.POST)
    public final ResponseEntity<Void> sendEvent(@PathVariable("streamName") final String streamName, @RequestBody final Object[] eventAttributes) throws InterruptedException {

        final RestEventConsumer restEventConsumer = registeredStreams.get(streamName);
        restEventConsumer.sendEvent(eventAttributes);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public void registerEventConsumer(final RestEventConsumer restEventConsumer) {

        final String streamName = restEventConsumer.getInputStreamName();
        registeredStreams.put(streamName, restEventConsumer);
    }

    public void unregisterEventConsumer(final RestEventConsumer restEventConsumer) {

        registeredStreams.remove(restEventConsumer.getInputStreamName());
    }
}
