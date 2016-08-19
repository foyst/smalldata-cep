package uk.co.foyst.smalldata.cep.consumer.transformer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.io.IOException;
import java.util.*;

public class OrderedJsonInboundEventTransformer implements InboundEventTransformer {

    final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object[] convertToObjectArray(byte[] message) {

        final String jsonString = new String(message);
        JsonNode jsonNode = null;

        try {
            jsonNode = objectMapper.readTree(jsonString);

        } catch (IOException e) {
            throw new RuntimeException("Exception whilst deserialising JSON object from Source:", e);
        }

        return transformFieldsToList(jsonNode).toArray();
    }

    private List<Object> transformFieldsToList(final JsonNode jsonNode) {
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        List<Object> outputList = new ArrayList<>();
        while (fields.hasNext()) {

            final Map.Entry<String, JsonNode> jsonFieldNode = fields.next();
            final List<Object> nodeValue = extractNodeValue(jsonFieldNode.getValue());
            outputList.addAll(nodeValue);
        }

        return outputList;
    }

    private List<Object> extractNodeValue(final JsonNode jsonFieldNode) {
        final JsonNodeType nodeType = jsonFieldNode.getNodeType();
        switch (nodeType) {
            case STRING: return Arrays.<Object>asList(jsonFieldNode.asText());
            case NUMBER:
                if (jsonFieldNode.isDouble()) return Arrays.<Object>asList(jsonFieldNode.asDouble());
                else return Arrays.<Object>asList(jsonFieldNode.asInt());
            case OBJECT:
                return transformFieldsToList(jsonFieldNode);
            case NULL:
                List<Object> nullElementList = new ArrayList<>();
                nullElementList.add(null);
                return nullElementList;
        }
        throw new IllegalArgumentException(String.format("Unknown JSON Data Type '%s'", nodeType));
    }
}
