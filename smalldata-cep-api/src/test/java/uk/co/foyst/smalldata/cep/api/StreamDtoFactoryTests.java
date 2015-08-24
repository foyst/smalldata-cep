package uk.co.foyst.smalldata.cep.api;

import org.junit.Test;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.StreamId;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StreamDtoFactoryTests {

    private final StreamDtoFactory streamDtoFactory = new StreamDtoFactory();

    @Test
    public void shouldConvertToStreamDtoGivenStream() {

        // Arrange
        final String streamIdString = "ccf3769f-3b7d-4be1-b96c-8b6bb9492219";
        final StreamId streamId = StreamId.fromString(streamIdString);
        final String name = "Football Possession Stream";
        final String definition = "define stream possessionStream(player string, team string, location string);";
        final String description = "Stream with possession of football relative to position on pitch";

        final Stream stream = new Stream(streamId, name, definition, description);

        // Act
        final StreamDto builtStreamDto = streamDtoFactory.build(stream);

        // Assert
        assertEquals(streamIdString, builtStreamDto.getStreamId());
        assertEquals(name, builtStreamDto.getName());
        assertEquals(definition, builtStreamDto.getDefinition());
        assertEquals(description, builtStreamDto.getDescription());
    }

    @Test
    public void shouldConvertToStreamDtosGivenListOfStreams() {

        // Arrange
        final String streamIdString = "ccf3769f-3b7d-4be1-b96c-8b6bb9492219";
        final StreamId streamId = StreamId.fromString(streamIdString);
        final String name = "Football Possession Stream";
        final String definition = "define stream possessionStream(player string, team string, location string);";
        final String description = "Stream with possession of football relative to position on pitch";

        final Stream stream = new Stream(streamId, name, definition, description);

        final String streamId2String = "dc9c8fd1-f1e5-4eaf-87ad-8275b7607c7e";
        final StreamId streamId2 = StreamId.fromString(streamId2String);
        final String name2 = "Player Fouls Stream";
        final String definition2 = "define stream playerFoulsStream(player string, severity string, location string);";
        final String description2 = "Stream with fouls committed by player";

        final Stream stream2 = new Stream(streamId2, name2, definition2, description2);

        // Act
        final List<StreamDto> builtStreamDtos = streamDtoFactory.build(Arrays.asList(stream, stream2));

        // Assert
        assertEquals(streamIdString, builtStreamDtos.get(0).getStreamId());
        assertEquals(name, builtStreamDtos.get(0).getName());
        assertEquals(definition, builtStreamDtos.get(0).getDefinition());
        assertEquals(description, builtStreamDtos.get(0).getDescription());

        assertEquals(streamId2String, builtStreamDtos.get(1).getStreamId());
        assertEquals(name2, builtStreamDtos.get(1).getName());
        assertEquals(definition2, builtStreamDtos.get(1).getDefinition());
        assertEquals(description2, builtStreamDtos.get(1).getDescription());
    }

    @Test
    public void shouldConvertToStreamGivenStreamDto() {

        // Arrange
        final String streamIdString = "ccf3769f-3b7d-4be1-b96c-8b6bb9492219";
        final StreamId streamId = StreamId.fromString(streamIdString);
        final String name = "Football Possession Stream";
        final String definition = "define stream possessionStream(player string, team string, location string);";
        final String description = "Stream with possession of football relative to position on pitch";

        final StreamDto streamDto = new StreamDto(streamIdString, name, definition, description);

        // Act
        final Stream builtStream = streamDtoFactory.convertToStream(streamDto);

        // Assert
        assertEquals(streamId, builtStream.getStreamId());
        assertEquals(name, builtStream.getName());
        assertEquals(definition, builtStream.getDefinition());
        assertEquals(description, builtStream.getDescription());
    }
}
