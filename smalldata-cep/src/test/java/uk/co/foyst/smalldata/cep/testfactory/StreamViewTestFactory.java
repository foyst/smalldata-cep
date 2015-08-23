package uk.co.foyst.smalldata.cep.testfactory;

import uk.co.foyst.smalldata.cep.StreamId;
import uk.co.foyst.smalldata.cep.dao.StreamView;

public class StreamViewTestFactory {

    public static StreamView buildTestInputStream() {

        final String streamId = new StreamId().toString();
        final String name = "TestInputStream";
        final String definition = "define stream TestInputStream(value1 string, value2 string);";
        final String description = "Test Stream for validating functionality";

        return new StreamView(streamId, name, definition, description);
    }
}
