package uk.co.foyst.smalldata.cep;

public interface CepEventObserver {
    public void receive(final CEPEvent[] events);
}
