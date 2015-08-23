package uk.co.foyst.smalldata.cep.dao;

import org.junit.Test;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.StreamId;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StreamViewFactoryTests {

    private final StreamViewFactory streamViewFactory = new StreamViewFactory();

    @Test
    public void shouldConvertToStreamViewGivenStream() {

        // Arrange
        final String streamIdString = "ccf3769f-3b7d-4be1-b96c-8b6bb9492219";
        final StreamId streamId = StreamId.fromString(streamIdString);
        final String name = "Football Possession Stream";
        final String definition = "define stream possessionStream(player string, team string, location string);";
        final String description = "Stream with possession of football relative to position on pitch";

        final Stream stream = new Stream(streamId, name, definition, description);

        // Act
        final StreamView builtStreamView = streamViewFactory.build(stream);

        // Assert
        assertEquals(streamIdString, builtStreamView.getStreamId());
        assertEquals(name, builtStreamView.getName());
        assertEquals(definition, builtStreamView.getDefinition());
        assertEquals(description, builtStreamView.getDescription());
    }

    @Test
    public void shouldConvertToStreamGivenStreamView() {

        // Arrange
        final String streamIdString = "ccf3769f-3b7d-4be1-b96c-8b6bb9492219";
        final StreamId streamId = StreamId.fromString(streamIdString);
        final String name = "Football Possession Stream";
        final String definition = "define stream possessionStream(player string, team string, location string);";
        final String description = "Stream with possession of football relative to position on pitch";

        final StreamView streamView = new StreamView(streamIdString, name, definition, description);

        // Act
        final Stream builtStream = streamViewFactory.convertToStream(streamView);

        // Assert
        assertEquals(streamId, builtStream.getStreamId());
        assertEquals(name, builtStream.getName());
        assertEquals(definition, builtStream.getDefinition());
        assertEquals(description, builtStream.getDescription());
    }

    @Test
    public void shouldConvertToStreamsGivenListOfStreamViews() {

        // Arrange
        final String streamIdString = "ccf3769f-3b7d-4be1-b96c-8b6bb9492219";
        final StreamId streamId = StreamId.fromString(streamIdString);
        final String name = "Football Possession Stream";
        final String definition = "define stream possessionStream(player string, team string, location string);";
        final String description = "Stream with possession of football relative to position on pitch";

        final StreamView streamView = new StreamView(streamIdString, name, definition, description);

        final String streamId2String = "dc9c8fd1-f1e5-4eaf-87ad-8275b7607c7e";
        final StreamId streamId2 = StreamId.fromString(streamId2String);
        final String name2 = "Player Fouls Stream";
        final String definition2 = "define stream playerFoulsStream(player string, severity string, location string);";
        final String description2 = "Stream with fouls committed by player";

        final StreamView streamView1 = new StreamView(streamId2String, name2, definition2, description2);

        // Act
        final List<Stream> builtStreams = streamViewFactory.convertToStream(Arrays.asList(streamView, streamView1));

        // Assert
        assertEquals(streamId, builtStreams.get(0).getStreamId());
        assertEquals(name, builtStreams.get(0).getName());
        assertEquals(definition, builtStreams.get(0).getDefinition());
        assertEquals(description, builtStreams.get(0).getDescription());

        assertEquals(streamId2, builtStreams.get(1).getStreamId());
        assertEquals(name2, builtStreams.get(1).getName());
        assertEquals(definition2, builtStreams.get(1).getDefinition());
        assertEquals(description2, builtStreams.get(1).getDescription());
    }
}
