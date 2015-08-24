package uk.co.foyst.smalldata.cep;

import java.util.UUID;

public class ScenarioId {

    private static final long serialVersionUID = 1L;

    final UUID scenarioId;

    public ScenarioId() {

        this(UUID.randomUUID());
    }

    public ScenarioId(final UUID value) {

        this.scenarioId = value;
    }

    public static ScenarioId fromString(final String value) {

        try {
            final UUID uuid = UUID.fromString(value);
            return new ScenarioId(uuid);
        } catch (final IllegalArgumentException ex) {
            throw new IllegalArgumentException("Cannot add new ScenarioId.  '" + value
                    + "' is not a valid UUID value.");
        }
    }

    @Override
    public String toString() {
        return scenarioId.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScenarioId that = (ScenarioId) o;

        return scenarioId.equals(that.scenarioId);

    }

    @Override
    public int hashCode() {
        return scenarioId.hashCode();
    }
}
