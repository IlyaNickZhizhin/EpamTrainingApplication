package org.epam.gymservice.model.gymModel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.epam.gymservice.model.User;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "trainers")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "specialization")
    private TrainingType specialization;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "trainees_trainers",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id"))
    private List<Trainee> trainees;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trainer", orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    private List<Training> trainings;

    public List<Training> getTrainings() {
        return new ArrayList<>(CollectionUtils.emptyIfNull(trainings));
    }

    public List<Trainee> getTrainees() {
        return new ArrayList<>(CollectionUtils.emptyIfNull(trainees));
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "id=" + id +
                ", specialization=" + specialization +
                ", trainings=" + trainings +
                '}';
    }
}
