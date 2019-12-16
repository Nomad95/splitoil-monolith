package com.splitoil.infrastructure.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public final class JacksonAdapter {
    private static final Logger log = LoggerFactory.getLogger(JacksonAdapter.class);
    public static final String EMPTY_MAP = "{}";
    public static final String EMPTY_COLLECTION = "[]";
    public static final String NULL_STR = "null";
    private static final JacksonAdapter instance = new JacksonAdapter();
    private final ObjectMapper mapper = createMapperWithAnnotationIntrospector(false);
    private final ObjectMapper mapperIndentOutput = createMapperWithAnnotationIntrospector(true);

    private JacksonAdapter() {
    }

    public static JacksonAdapter getInstance() {
        return instance;
    }

    public String toJson(Object object) {
        return this.toJson(object, false);
    }

    public String toJson(Object object, boolean formatJSON) {
        if (object != null && object != "") {
            try {
                return formatJSON ? this.mapperIndentOutput.writeValueAsString(object) : this.mapper.writeValueAsString(object);
            } catch (Exception var4) {
                this.handleJsonEncodingError(object, var4);
                throw new JsonEncodingException(var4);
            }
        } else {
            return "null";
        }
    }

    public <T> T jsonDecode(String json, Class<T> clazz) {
        JavaType javaType = this.createFromCanonical(clazz.getCanonicalName());
        return this.jsonDecodeByJavaType(json, javaType);
    }

    public <T> T jsonDecode(String json, TypeReference<T> refType) {
        JavaType type = this.getTypeFactory().constructType(refType);
        return this.jsonDecodeByJavaType(json, type);
    }

    public <T> T jsonDecodeNode(JsonNode jsonNode, TypeReference<T> refType) {
        return this.jsonDecode(jsonNode.toString(), refType);
    }

    public <T> T jsonDecodeNode(JsonNode jsonNode, Class<T> clazz) {
        JavaType javaType = this.createFromCanonical(clazz.getCanonicalName());
        return this.jsonDecodeByJavaType(jsonNode.toString(), javaType);
    }

    public <T> T jsonDecodeByJavaType(String json, JavaType type) {
        try {
            if (this.isJsonEqNull(json)) {
                return this.createEmptyResult(json, type);
            } else {
                T result = this.mapper.readValue(json, type);
                return result;
            }
        } catch (Exception var4) {
            this.handleJsonDecodingError(json, var4);
            throw new JsonDecodingException(var4);
        }
    }

    @SneakyThrows
    private <T> T createEmptyResult(String json, JavaType type) {
        try {
            StringBuilder emptyJson = new StringBuilder("");
            if (type.isCollectionLikeType()) {
                emptyJson.append("[]");
            } else {
                if (!type.isMapLikeType()) {
                    return null;
                }

                emptyJson.append("{}");
            }

            T emptyObject = this.mapper.readValue(emptyJson.toString(), type);
            return emptyObject;
        } catch (Throwable var5) {
            throw var5;
        }
    }

    private boolean isJsonEqNull(String json) {
        return StringUtils.trimToNull(json) == null ? true : this.jsonDecodeTree(json).isNull();
    }

    public static ObjectMapper createMapperWithAnnotationIntrospector(boolean indentOutput) {
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false);
        objectMapper.getDeserializationConfig().with(introspector);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        objectMapper.registerModule(NumberSerializeModule.getInstance());
        objectMapper.getSerializationConfig().with(introspector);
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.PUBLIC_ONLY);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);
        if (indentOutput) {
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        }

        return objectMapper;
    }

    public JsonNode jsonDecodeTree(String json) {
        try {
            return this.mapper.readTree(json);
        } catch (Exception var3) {
            this.handleJsonDecodingError(json, var3);
            throw new JsonDecodingException(var3);
        }
    }

    private void handleJsonEncodingError(Object object, Exception e) {
        log.error("JSON encoding error! Provided object to encode: {}", new Object[]{object, e});
    }

    private void handleJsonDecodingError(String json, Exception e) throws RuntimeException {
        log.error("JSON decoding error! Provided json to decode: {}", new Object[]{json, e});
    }

    public <T> T cast(Object object, TypeReference<T> refType) {
        String json = this.toJson(object);
        return this.jsonDecode(json, refType);
    }

    public Object readValue(Class<? extends Collection> collectionClass, Class<?> elementClass, String textValue) throws IOException {
        JavaType type = this.createCollectionLikeType(collectionClass, elementClass);
        return this.mapper.readValue(textValue, type);
    }

    private TypeFactory getTypeFactory() {
        return this.mapper.getTypeFactory();
    }

    public JavaType createCollectionLikeType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return this.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    public JavaType createMapLikeType(Class<? extends Map> mapClass, Class<?> keyType, Class<?> valueType) {
        return this.getTypeFactory().constructMapLikeType(mapClass, keyType, valueType);
    }

    public JavaType createFromCanonical(String canonicalName) {
        return this.getTypeFactory().constructFromCanonical(canonicalName);
    }

    public boolean isValidJSONString(Object value) {
        if (value instanceof String) {
            String jsonStr = value.toString();

            try {
                return this.jsonDecodeTree(jsonStr) != null;
            } catch (Exception var4) {
                log.debug("Provided object is not valid JSON string", var4);
            }
        }

        return false;
    }
}
