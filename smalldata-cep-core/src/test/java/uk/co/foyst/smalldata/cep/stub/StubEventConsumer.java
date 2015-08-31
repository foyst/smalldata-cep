package uk.co.foyst.smalldata.cep.stub;

import uk.co.foyst.smalldata.cep.consumer.EventConsumer;

public class StubEventConsumer implements EventConsumer {

    private boolean started = false;

    @Override
    public void start() {
        started = true;
    }

    @Override
    public void stop() {
        started = false;
    }

    @Override
    public boolean isStarted() {
        return started;
    }
}
