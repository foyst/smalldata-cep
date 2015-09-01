package uk.co.foyst.smalldata.cep.testfactory;

import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.StreamId;

public class StreamTestFactory {

    public static Stream buildTestInputStream() {

        StreamId streamId = new StreamId();
        String name = "TestInputStream";
        String definition = "define stream TestInputStream(value1 string, value2 string);";
        String description = "Test Stream for validating functionality";

        return new Stream(streamId, name, definition, description);
    }
}
