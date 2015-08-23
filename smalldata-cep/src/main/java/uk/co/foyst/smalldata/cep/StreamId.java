package uk.co.foyst.smalldata.cep;

import java.util.UUID;

public class StreamId {

    private static final long serialVersionUID = 1L;

    final UUID streamId;

    public StreamId() {

        this(UUID.randomUUID());
    }

    public StreamId(final UUID value) {

        this.streamId = value;
    }

    public static StreamId fromString(final String value) {

        try {
            final UUID uuid = UUID.fromString(value);
            return new StreamId(uuid);
        } catch (final IllegalArgumentException ex) {
            throw new IllegalArgumentException("Cannot add new StreamId.  '" + value
                    + "' is not a valid UUID value.");
        }
    }

    @Override
    public String toString() {
        return streamId.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StreamId streamId = (StreamId) o;

        return this.streamId.equals(streamId.streamId);

    }

    @Override
    public int hashCode() {
        return streamId.hashCode();
    }
}
