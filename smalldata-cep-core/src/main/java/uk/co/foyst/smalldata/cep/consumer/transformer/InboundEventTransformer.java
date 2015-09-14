package uk.co.foyst.smalldata.cep.consumer.transformer;

public interface InboundEventTransformer {
    Object[] convertToObjectArray(byte[] message);
}
