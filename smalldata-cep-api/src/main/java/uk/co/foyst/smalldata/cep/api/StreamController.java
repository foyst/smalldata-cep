package uk.co.foyst.smalldata.cep.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.StreamId;
import uk.co.foyst.smalldata.cep.service.StreamService;

import java.util.List;

@Controller
@RequestMapping(value = "/v1/streams")
public class StreamController {

    public static final String MISSING_STREAM_MESSAGE = "Stream '%s' does not exist.";
    private final Logger log = LoggerFactory.getLogger(StreamController.class);

    private final StreamService streamService;
    private final StreamDtoFactory streamDtoFactory;

    @Autowired
    public StreamController(StreamService streamService, StreamDtoFactory streamDtoFactory) {
        this.streamService = streamService;
        this.streamDtoFactory = streamDtoFactory;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public final ResponseEntity<List<StreamDto>> get() throws Exception {

        final String messageFormat = "No Streams found.";
        final List<Stream> existingStreams = streamService.readAll();

        Assert.notNull(existingStreams, String.format(messageFormat));

        final List<StreamDto> streamDtos = streamDtoFactory.build(existingStreams);

        return new ResponseEntity<>(streamDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    @ResponseBody
    public final ResponseEntity<StreamDto> getById(@PathVariable("key") final String streamId) throws Exception {

        final StreamId id = StreamId.fromString(streamId);
        final Stream foundStream = streamService.read(id);
        Assert.notNull(foundStream, String.format(MISSING_STREAM_MESSAGE, streamId));
        final StreamDto streamDto = streamDtoFactory.build(foundStream);
        return new ResponseEntity<>(streamDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public final ResponseEntity<Void> create(@RequestBody final StreamDto streamDto) throws Exception {

        log.info("add: streamDto={}", streamDto);

        final Stream newStream = streamDtoFactory.convertToStream(streamDto);

        final Stream createdStream = streamService.add(newStream);

        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ControllerLinkBuilder.linkTo(StreamController.class).slash(createdStream.getStreamId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public final ResponseEntity<StreamDto> update(@RequestBody final StreamDto streamDto) throws Exception {

        final String messageFormat = "Stream '%s' could not be updated.";
        final StreamId streamId = StreamId.fromString(streamDto.getStreamId());
        Assert.notNull(streamDto, String.format(messageFormat, streamId));

        final Stream stream = streamDtoFactory.convertToStream(streamDto);
        final Stream updatedStream = streamService.update(stream);

        final StreamDto updatedStreamDto = streamDtoFactory.build(updatedStream);
        return new ResponseEntity<>(updatedStreamDto, HttpStatus.OK);
    }
}
