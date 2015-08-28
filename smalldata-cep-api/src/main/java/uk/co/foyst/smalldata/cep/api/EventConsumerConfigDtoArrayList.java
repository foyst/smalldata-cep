package uk.co.foyst.smalldata.cep.api;

import java.util.ArrayList;
import java.util.Collection;

public class EventConsumerConfigDtoArrayList extends ArrayList<EventConsumerConfigDto> {

    public EventConsumerConfigDtoArrayList(Collection<? extends EventConsumerConfigDto> collection) {

        super(collection);
    }

}