package uz.lb.fxchatserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonObjectConvert {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T convertToObject(Class<T> obj, String json) {
        try {
            return mapper.readValue(json, obj);
        } catch (JsonProcessingException e) {
            log.error("JsonObjectConvert.convertToObject => {}", e.getMessage());
            return null;
        }
    }

    public static String convertToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("JsonObjectConvert.convertToJson => {}", e.getMessage());
            return null;
        }
    }
}
