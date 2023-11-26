package org.epam.model.gymModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.epam.model.User;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainers")
public class Trainer {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "specialization")
    private TrainingType specialization;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trainer",
            cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    private List<Training> trainings;

    @Override
    public String toString() {
        return "Trainer{" +
                "id=" + id +
                ", specialization=" + specialization +
                ", user=" + user +
                ", trainings=" + trainings +
                '}';
    }
}
