package com.konivax.utils.format;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public final class YamlUtils {

    private YamlUtils() { }

    public static <T> T deserializeObject(String yamlString, Class<T> clazz) {
        if(yamlString == null)
            return null;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(yamlString, clazz);
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T deserializeFile(String yamlFile, Class<T> clazz) {
        if(yamlFile == null)
            return null;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(new File(yamlFile), clazz);
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
