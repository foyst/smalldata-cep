package uk.co.foyst.smalldata.cep.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;
import uk.co.foyst.smalldata.cep.dao.AbstractEventConsumerConfigViewFactory;
import uk.co.foyst.smalldata.cep.dao.EventConsumerConfigView;
import uk.co.foyst.smalldata.cep.dao.EventConsumerConfigViewDao;
import uk.co.foyst.smalldata.cep.exception.EntityNotFoundException;

import java.util.List;

@Service
public class EventConsumerConfigService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String NOT_EXISTS_MESSAGE = "Event Consumer Config with Id '%s' does not exist.";

    private final EventConsumerConfigViewDao eventConsumerConfigViewDao;
    private final AbstractEventConsumerConfigViewFactory eventConsumerConfigViewFactory;

    @Autowired
    public EventConsumerConfigService(EventConsumerConfigViewDao eventConsumerConfigViewDao, AbstractEventConsumerConfigViewFactory eventConsumerConfigViewFactory) {
        this.eventConsumerConfigViewDao = eventConsumerConfigViewDao;
        this.eventConsumerConfigViewFactory = eventConsumerConfigViewFactory;
    }

    public List<EventConsumerConfig> readAll() {

        return eventConsumerConfigViewFactory.convertToEventConsumerConfig(eventConsumerConfigViewDao.findAll());
    }

    public EventConsumerConfig create(final EventConsumerConfig eventConsumerConfig) {

        log.info("add: eventConsumerConfig={}", eventConsumerConfig);

        final EventConsumerConfigView eventConsumerConfigView = eventConsumerConfigViewFactory.build(eventConsumerConfig);
        log.info("add: eventConsumerConfigView={}", eventConsumerConfigView);
        eventConsumerConfigViewDao.save(eventConsumerConfigView);

        log.info("add: Returning: {}", eventConsumerConfig);
        return eventConsumerConfig;
    }

    public EventConsumerConfig read(final EventConsumerId id) {

        final String eventConsumerId = id.toString();
        final EventConsumerConfigView eventConsumerConfigView = eventConsumerConfigViewDao.findOne(eventConsumerId);
        if (eventConsumerConfigView == null) {
            throw new EntityNotFoundException(String.format(NOT_EXISTS_MESSAGE, id.toString()));
        }
        return eventConsumerConfigViewFactory.convertToEventConsumerConfig(eventConsumerConfigView);
    }

    public EventConsumerConfig update(final EventConsumerConfig updatedEventConsumerConfig) {

        final EventConsumerConfigView currentEventConsumerConfigView = eventConsumerConfigViewDao.findOne(updatedEventConsumerConfig.getEventConsumerId().toString());
        if (currentEventConsumerConfigView == null) {
            throw new EntityNotFoundException(String.format(NOT_EXISTS_MESSAGE, updatedEventConsumerConfig.getEventConsumerId().toString()));
        }

        final EventConsumerConfigView eventConsumerConfigView = eventConsumerConfigViewFactory.build(updatedEventConsumerConfig);
        eventConsumerConfigViewDao.save(eventConsumerConfigView);

        return updatedEventConsumerConfig;
    }

    public void delete(final EventConsumerConfig eventConsumerConfig) {

        final EventConsumerConfigView eventConsumerConfigView = eventConsumerConfigViewFactory.build(eventConsumerConfig);
        eventConsumerConfigViewDao.delete(eventConsumerConfigView);
    }
}
