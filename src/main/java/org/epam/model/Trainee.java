package org.epam.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.ToString;

import java.util.Date;
import java.util.Optional;

@ToString
public class Trainee implements Model {
    private int id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Optional<Date> dateOfBirth;

    private Optional<String> address;

    private int userId;

    public Trainee() {}

    public Trainee(int userId) {
        this.userId = userId;
    }

    public Trainee(Date dateOfBirth, int userId) {
        this (userId);
        this.dateOfBirth = Optional.ofNullable(dateOfBirth);
    }

    public Trainee(String address, int userId) {
        this (userId);
        this.address = Optional.ofNullable(address);
    }

    public Trainee(Date dateOfBirth, String address, int userId) {
        this (dateOfBirth, userId);
        this.address = Optional.ofNullable(address);
    }

    public int getId() {
        return id;
    }

    public Date getDateOfBirth() {
        return dateOfBirth.get();
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = Optional.ofNullable(dateOfBirth);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address = Optional.ofNullable(address);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
