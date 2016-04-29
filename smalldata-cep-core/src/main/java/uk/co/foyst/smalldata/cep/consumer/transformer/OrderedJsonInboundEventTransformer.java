package uk.co.foyst.smalldata.cep.consumer.transformer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OrderedJsonInboundEventTransformer implements InboundEventTransformer {

    final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object[] convertToObjectArray(byte[] message) {

        final String jsonString = new String(message);
        Iterator<Map.Entry<String, JsonNode>> fields = null;
        List<Object> outputList = new ArrayList<>();

        try {
            final JsonNode jsonNode = objectMapper.readTree(jsonString);
            fields = jsonNode.fields();
        } catch (IOException e) {
            throw new RuntimeException("Exception whilst deserialising JSON object from Source:", e);
        }

        while (fields.hasNext()) {

            final Map.Entry<String, JsonNode> jsonFieldNode = fields.next();
            final Object nodeValue = extractNodeValue(jsonFieldNode.getValue());
            outputList.add(nodeValue);
        }

        return outputList.toArray();
    }

    private Object extractNodeValue(final JsonNode jsonFieldNode) {
        final JsonNodeType nodeType = jsonFieldNode.getNodeType();
        switch (nodeType) {
            case STRING: return jsonFieldNode.asText();
            case NUMBER:
                if (jsonFieldNode.isDouble()) return jsonFieldNode.asDouble();
                else return jsonFieldNode.asInt();
            case NULL: return null;
        }
        throw new IllegalArgumentException(String.format("Unknown JSON Data Type '%s'", nodeType));
    }
}
