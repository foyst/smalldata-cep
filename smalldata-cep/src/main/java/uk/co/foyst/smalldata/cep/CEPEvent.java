package uk.co.foyst.smalldata.cep;

public class CEPEvent {
    private final String streamId;
    private final long timeStamp;
    private final Object[] data;

    public CEPEvent(String streamId, long timeStamp, Object[] data) {
        this.streamId = streamId;
        this.timeStamp = timeStamp;
        this.data = data;
    }

    public String getStreamId() {
        return streamId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public Object[] getData() {
        return data;
    }
}
