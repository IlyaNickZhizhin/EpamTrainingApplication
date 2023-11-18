package org.epam.model.gymModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.epam.model.User;

import static jakarta.persistence.GenerationType.IDENTITY;

@EqualsAndHashCode
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "trainers")
public class Trainer implements Model, UserSetter {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "specialization")
    private TrainingType specialization;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    public Trainer(TrainingType specialization, User user) {
        this.specialization = specialization;
        this.user = user;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    @Override
    public String getEntityName() {
        return "trainers";
    }

}
