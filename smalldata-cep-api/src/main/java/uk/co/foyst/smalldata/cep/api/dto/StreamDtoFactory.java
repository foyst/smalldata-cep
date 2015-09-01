package uk.co.foyst.smalldata.cep.api.dto;

import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.StreamId;

import java.util.ArrayList;
import java.util.List;

@Component
public class StreamDtoFactory {

    public StreamDto build(final Stream stream) {

        return new StreamDto(stream.getStreamId().toString(), stream.getName(), stream.getDefinition(), stream.getDescription());
    }

    public Stream convertToStream(final StreamDto streamDto) {

        return new Stream(StreamId.fromString(streamDto.getStreamId()), streamDto.getName(), streamDto.getDefinition(), streamDto.getDescription());
    }

    public List<StreamDto> build(List<Stream> streams) {

        final List<StreamDto> builtStreamDtos = new ArrayList<>(streams.size());
        for (final Stream stream : streams)
            builtStreamDtos.add(build(stream));

        return builtStreamDtos;
    }
}
