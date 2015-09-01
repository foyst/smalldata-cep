package uk.co.foyst.smalldata.cep.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.adapter.CEPAdapter;

import java.util.HashMap;

@Controller
@RequestMapping(value = "/v1/restEventConsumer")
public class RestEventConsumerController implements EventConsumer {

    private final CEPAdapter cepAdapter;
    private final HashMap<String, Stream> registeredStreams;

    @Autowired
    public RestEventConsumerController(CEPAdapter cepAdapter) {
        this.cepAdapter = cepAdapter;
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

    public void registerEventConsumer(String streamName, Stream stream) {

        registeredStreams.put(streamName, stream);
    }

    @RequestMapping(value = "/{streamName}", method = RequestMethod.POST)
    public final ResponseEntity<Void> sendEvent(@PathVariable("streamName") final String streamName, @RequestBody final Object[] eventAttributes) throws InterruptedException {

        final Stream stream = registeredStreams.get(streamName);
        cepAdapter.sendEvent(stream, eventAttributes);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
