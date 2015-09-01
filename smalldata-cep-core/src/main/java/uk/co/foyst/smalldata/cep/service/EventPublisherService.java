package uk.co.foyst.smalldata.cep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.foyst.smalldata.cep.adapter.CEPAdapter;
import uk.co.foyst.smalldata.cep.publisher.EventPublisher;

@Service
public class EventPublisherService {

    private final CEPAdapter cepAdapter;

    @Autowired
    public EventPublisherService(CEPAdapter cepAdapter) {
        this.cepAdapter = cepAdapter;
    }

    public void registerEventPublisher(EventPublisher eventPublisher) {

        cepAdapter.addStreamListener(eventPublisher);
    }
}
