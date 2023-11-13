package org.epam.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Trainer implements Model {
    private int id;
    private String specialization;

    private int userId;

    public int getId() {
        return id;
    }

    public Trainer() {}

    public Trainer(String specialization, int userId) {
        this.specialization = specialization;
        this.userId = userId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
