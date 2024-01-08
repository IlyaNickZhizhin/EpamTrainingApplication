package org.epam.reportservice.repository;

import lombok.Getter;
import org.epam.reportservice.model.TrainerKey;
import org.epam.reportservice.model.TrainingSession;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Getter
public class WorkloadStorage {
    private final Map<TrainerKey, Queue<TrainingSession>> storage = new HashMap<>();

    public Queue<TrainingSession> addWorkload(TrainerKey key, TrainingSession newSession){
        if (!storage.containsKey(key)){
            PriorityQueue<TrainingSession> queue = new PriorityQueue<>();
            queue.add(newSession);
            storage.put(key, queue);
            return queue;
        }
        for (TrainingSession session : storage.get(key)) {
            if (session.getYear().equals(newSession.getYear()) && session.getMonth().equals(newSession.getMonth())) {
                session.setDuration(session.getDuration()+newSession.getDuration());
                return storage.get(key);
            }
        }
        storage.get(key).add(newSession);
        return storage.get(key);
    }

    public Queue<TrainingSession> deleteWorkload(TrainerKey key, TrainingSession newSession){
        if (!storage.containsKey(key)){
            throw new NoSuchElementException("Trainer " + key.getFirstName() + " " + key.getLastName().charAt(0) + "***" +
                    "has no training workload");
        }
        for (TrainingSession session : storage.get(key)) {
            if (session.getYear().equals(newSession.getYear()) && session.getMonth().equals(newSession.getMonth())) {
                if (session.getDuration() <= newSession.getDuration()) storage.get(key).remove(session);
                else session.setDuration(session.getDuration()-newSession.getDuration());
                return storage.get(key);
            }
        }
        return storage.get(key);
    }
}
