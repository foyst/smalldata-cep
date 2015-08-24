package uk.co.foyst.smalldata.cep.consumer;

public interface EventConsumer {

    void start();
    void stop();
    boolean isStarted();
}
