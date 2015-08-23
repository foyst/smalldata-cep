package uk.co.foyst.smalldata.cep.dao;

import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.StreamId;

import java.util.ArrayList;
import java.util.List;

@Component
public class StreamViewFactory {

    public StreamView build(final Stream stream) {

        return new StreamView(stream.getStreamId().toString(), stream.getName(), stream.getDefinition(), stream.getDescription());
    }

    public Stream convertToStream(final StreamView view) {

        final StreamId streamId = StreamId.fromString(view.getStreamId());

        return new Stream(streamId, view.getName(), view.getDefinition(), view.getDescription());
    }

    public List<Stream> convertToStream(final List<StreamView> streamViews) {

        final List<Stream> streams = new ArrayList<>(streamViews.size());

        for (final StreamView streamView : streamViews) {
            streams.add(convertToStream(streamView));
        }

        return streams;
    }
}
