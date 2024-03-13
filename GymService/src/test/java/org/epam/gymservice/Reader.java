package org.epam.gymservice;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.function.Function;

public class Reader {

    static String startPath ="";
    static String endPath ="";

    public void setStartPath(String startPath) {
        Reader.startPath = startPath;
    }

    public void setEndPath(String endPath) {
        Reader.endPath = endPath;
    }

    public Reader(String startPath, String endPath) {
        setStartPath(startPath);
        setEndPath(endPath);
    }

    public Reader() {
    }

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

    public<D, E> D readDto(String path, Class<E> clazz, Function<E, D> mapper) {
        E entity = readEntity(path, clazz);
        return mapper.apply(entity);
    }

}
