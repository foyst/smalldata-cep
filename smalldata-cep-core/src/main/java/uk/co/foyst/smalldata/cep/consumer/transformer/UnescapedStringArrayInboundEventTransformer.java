package uk.co.foyst.smalldata.cep.consumer.transformer;

public class UnescapedStringArrayInboundEventTransformer implements InboundEventTransformer {

    @Override
    public Object[] convertToObjectArray(byte[] message) {

        final String attributeArrayString = new String(message);
        final String[] stringAttributes = attributeArrayString.replace("[", "").replace("]", "").split(",");
        final Object[] parsedAttributes = new Object[stringAttributes.length];

        for (int i = 0; i < stringAttributes.length; i++) {

            Double parsedInt = null;
            try {
                parsedInt = Double.parseDouble(stringAttributes[i]);
            } catch (NumberFormatException e) {}
            if (parsedInt == null)
                parsedAttributes[i] = stringAttributes[i];
            else
                parsedAttributes[i] = parsedInt;
        }

        return parsedAttributes;
    }
}
