package com.konivax.utils.format;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public final class JsonUtils {

    private JsonUtils() { }

    public static <T> T deserializeFile(String jsonFile, Class<T> clazz) {
        if(jsonFile == null)
            return null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(jsonFile), clazz);
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T deserializeJavaObject(String jsonString, Class<T> clazz) {
        if(jsonString == null)
            return null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonString, clazz);
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <K,V> Map<K,V> deserializeJavaObjectMap(String jsonString, Class<K> keyClazz, Class<V> valClazz) {
        if(jsonString == null)
            return null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonString, mapper.getTypeFactory().constructMapType(Map.class, keyClazz, valClazz));
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serializeJavaObject(Object object) {
        if(object == null)
            return null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch(JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
