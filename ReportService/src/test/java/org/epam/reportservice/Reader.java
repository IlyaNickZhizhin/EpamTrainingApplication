package org.epam.reportservice;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

public class Reader {

    static String startPath ="";
    static String endPath ="";

    public<M> M readEntity(String path, Class<M> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        try {
            InputStream inputStream = new FileInputStream(startPath + path + endPath);
            return objectMapper.readValue(inputStream, clazz);
        } catch (IOException e) {
            System.out.println("Error while reading file " + path);
            return null;
        }
    }
}
