package uk.co.foyst.smalldata.cep;

public interface OutputListener {
    public void receive(final CEPEvent[] events);
}
