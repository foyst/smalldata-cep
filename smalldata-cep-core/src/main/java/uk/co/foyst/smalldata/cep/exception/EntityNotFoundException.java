package uk.co.foyst.smalldata.cep.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
