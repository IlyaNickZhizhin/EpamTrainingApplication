package org.epam.model.gymModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import org.epam.model.User;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Role {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;
}
