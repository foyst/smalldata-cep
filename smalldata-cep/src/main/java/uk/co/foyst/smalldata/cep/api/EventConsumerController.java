package uk.co.foyst.smalldata.cep.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;
import uk.co.foyst.smalldata.cep.service.EventConsumerService;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/v1/eventConsumer")
public class EventConsumerController {

    public static final String EVENT_CONSUMER_CREATION_ERROR_MESSAGE = "Event Consumer '%s' could not be created.";
    private final Logger log = LoggerFactory.getLogger(EventConsumerController.class);

    private final EventConsumerService eventConsumerService;
    private final AbstractEventConsumerConfigDtoFactory abstractEventConsumerConfigDtoFactory;

    @Autowired
    public EventConsumerController(EventConsumerService eventConsumerService, AbstractEventConsumerConfigDtoFactory abstractEventConsumerConfigDtoFactory) {
        this.eventConsumerService = eventConsumerService;
        this.abstractEventConsumerConfigDtoFactory = abstractEventConsumerConfigDtoFactory;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public final ResponseEntity<List<EventConsumerConfigDto>> get() throws Exception {

        final String messageFormat = "No Event Consumer Configurations found.";
        final List<EventConsumerConfig> existingEventConsumerConfigs = eventConsumerService.readAll();

        Assert.notNull(existingEventConsumerConfigs, String.format(messageFormat));

        final List<EventConsumerConfigDto> eventConsumerConfigDtos = abstractEventConsumerConfigDtoFactory.build(existingEventConsumerConfigs);

        return new ResponseEntity<>(eventConsumerConfigDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    @ResponseBody
    public final ResponseEntity<EventConsumerConfigDto> getById(@PathVariable("key") final String eventConsumerId) throws Exception {

        final EventConsumerId id = EventConsumerId.fromString(eventConsumerId);
        final EventConsumerConfig eventConsumerConfig = eventConsumerService.read(id);
        final EventConsumerConfigDto scenarioDto = abstractEventConsumerConfigDtoFactory.build(eventConsumerConfig);
        return new ResponseEntity<>(scenarioDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public final ResponseEntity<Void> create(@RequestBody final EventConsumerConfigDto eventConsumerConfigDto) throws Exception {

        log.info("add: eventConsumerConfigDto={}", eventConsumerConfigDto);

        final EventConsumerConfig eventConsumerConfig = abstractEventConsumerConfigDtoFactory.convertToEventConsumerConfig(eventConsumerConfigDto);
        log.info("add: eventConsumerConfig={}", eventConsumerConfig);

        EventConsumerConfig createdEventConsumerConfig;
        try {
            createdEventConsumerConfig = eventConsumerService.create(eventConsumerConfig);
        } catch (Exception e) {
            throw new Exception(String.format(EVENT_CONSUMER_CREATION_ERROR_MESSAGE, eventConsumerConfig.getEventConsumerId().toString()), e);
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(EventConsumerController.class).slash(createdEventConsumerConfig.getEventConsumerId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public final ResponseEntity<EventConsumerConfigDto> update(@RequestBody final EventConsumerConfigDto eventConsumerConfigDto) throws Exception {

        final String messageFormat = "Event Consumer '%s' could not be updated.";
        Assert.notNull(eventConsumerConfigDto, String.format(messageFormat, eventConsumerConfigDto.getEventConsumerId()));

        final EventConsumerConfig eventConsumerConfig = abstractEventConsumerConfigDtoFactory.convertToEventConsumerConfig(eventConsumerConfigDto);

        final EventConsumerConfig updatedEventConsumerConfig = eventConsumerService.update(eventConsumerConfig);

        final EventConsumerConfigDto updatedEventConsumerConfigDto = abstractEventConsumerConfigDtoFactory.build(updatedEventConsumerConfig);
        return new ResponseEntity<>(updatedEventConsumerConfigDto, HttpStatus.OK);
    }
}