package org.epam.reader;

import org.epam.Reader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class ReaderTest {


    @Test
    public void testReadUser() throws IOException {
        Stream<Path> usersDirect = Files.walk(Paths.get("src/test/resources/models/users"));
        Stream<Path> typeDirect = Files.walk(Paths.get("src/test/resources/models/trainingtypes"));
        Stream<Path> trainerDirect = Files.walk(Paths.get("src/test/resources/models/trainers"));
        Stream<Path> traineeDirect = Files.walk(Paths.get("src/test/resources/models/trainees"));
        Stream<Path> trainingDirect = Files.walk(Paths.get("src/test/resources/models/trainings"));
        usersDirect.forEach(p -> new Reader().readUser(p.toString()));
        typeDirect.forEach(p -> new Reader().readType(p.toString()));
        trainerDirect.forEach(p -> new Reader().readTrainer(p.toString()));
        traineeDirect.forEach(p -> new Reader().readTrainee(p.toString()));
        trainingDirect.forEach(p -> new Reader().readTraining(p.toString()));
    }
}
