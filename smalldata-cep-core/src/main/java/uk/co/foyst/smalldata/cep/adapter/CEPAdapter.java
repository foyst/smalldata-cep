package uk.co.foyst.smalldata.cep.adapter;

import uk.co.foyst.smalldata.cep.CEPEventObserver;
import uk.co.foyst.smalldata.cep.Scenario;
import uk.co.foyst.smalldata.cep.Stream;

public interface CEPAdapter {

    void addStreamListener(final CEPEventObserver listener);

    void sendEvents(final Stream inputStream, final Object[][] events) throws InterruptedException;

    void sendEvent(final Stream inputStream, final Object[] event) throws InterruptedException;

    void define(final Scenario scenario);

    void update(final Scenario scenario);

    void remove(final Scenario scenario);

    void define(final Stream stream);

    void update(final Stream updatedStream);

    void remove(final Stream stream);
}
