package uk.co.foyst.smalldata.cep;

public interface CEPEventObserver {
    public void receive(final CEPEvent[] events);
}
