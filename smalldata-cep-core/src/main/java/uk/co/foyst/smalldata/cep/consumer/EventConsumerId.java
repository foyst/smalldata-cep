package uk.co.foyst.smalldata.cep.consumer;

import java.util.UUID;

public class EventConsumerId {

    private static final long serialVersionUID = 1L;

    final UUID cepEventConsumerId;

    public EventConsumerId() {

        this(UUID.randomUUID());
    }

    public EventConsumerId(final UUID value) {

        this.cepEventConsumerId = value;
    }

    public static EventConsumerId fromString(final String value) {

        try {
            final UUID uuid = UUID.fromString(value);
            return new EventConsumerId(uuid);
        } catch (final IllegalArgumentException ex) {
            throw new IllegalArgumentException("Cannot add new CEPEventConsumerId.  '" + value
                    + "' is not a valid UUID value.");
        }
    }

    @Override
    public String toString() {
        return cepEventConsumerId.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventConsumerId that = (EventConsumerId) o;

        return cepEventConsumerId.equals(that.cepEventConsumerId);

    }

    @Override
    public int hashCode() {
        return cepEventConsumerId.hashCode();
    }
}
