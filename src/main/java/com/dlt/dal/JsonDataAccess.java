package com.dlt.dal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JsonDataAccess {

    private final ObjectMapper objectMapper;
    private final Map<String, List<?>> dataCache = new ConcurrentHashMap<>();
    private static final String DATA_DIR = "data";

    @Autowired
    public JsonDataAccess(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        File dataDirectory = new File(DATA_DIR);
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }
    }

    public <T> List<T> readData(String fileName, TypeReference<List<T>> typeReference) throws IOException {
        File file = new File(DATA_DIR, fileName);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(file, typeReference);
    }

    public <T> void writeData(String fileName, List<T> data) throws IOException {
        File file = new File(DATA_DIR, fileName);
        objectMapper.writeValue(file, data);
    }
}
