package uk.co.foyst.smalldata.cep.api;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

public class EventConsumerConfigDtoTypeIdResolver implements TypeIdResolver {

    private JavaType configBaseType;
    private final Set<JavaType> configSubTypes = new HashSet<>();

    @Override
    public void init(JavaType baseType) {

        this.configBaseType = TypeFactory.defaultInstance().constructType(EventConsumerConfigDto.class);
        final Reflections reflections = new Reflections("uk.co.foyst.smalldata.cep.api");
        final Set<Class<? extends EventConsumerConfigDto>> configSubClasses = reflections.getSubTypesOf(EventConsumerConfigDto.class);

        for (final Class<? extends EventConsumerConfigDto> configSubType : configSubClasses)
            configSubTypes.add(TypeFactory.defaultInstance().constructSpecializedType(configBaseType, configSubType));
    }

    @Override
    public String idFromValue(Object value) {

        return value.getClass().getSimpleName();
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {

        return idFromValue(value);
    }

    @Override
    public String idFromBaseType() {

        return idFromValueAndType(null, configBaseType.getRawClass());
    }

    @Override
    public JavaType typeFromId(String id) {

        for (final JavaType configSubType : configSubTypes)
            if (id.equals(configSubType.getRawClass().getSimpleName()))
                return configSubType;

        throw new IllegalArgumentException("Cannot find Dto class " + id);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}