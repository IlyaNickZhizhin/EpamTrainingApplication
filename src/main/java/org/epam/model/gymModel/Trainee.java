package org.epam.model.gymModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;
import java.util.Optional;

@EqualsAndHashCode
@Getter
public class Trainee implements Model {

    private static final Date DEFAULT_BIRTH_DATE = new Date(0);
    private static final String DEFAULT_ADDRESS = "not defined yet";

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

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public Date getDateOfBirth() {
        if (dateOfBirth == null) dateOfBirth = Optional.ofNullable(DEFAULT_BIRTH_DATE);
        return dateOfBirth.isPresent() ? dateOfBirth.get() : DEFAULT_BIRTH_DATE;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = Optional.ofNullable(dateOfBirth);
    }

    public String getAddress() {
        if (address == null) address = Optional.ofNullable(DEFAULT_ADDRESS);
        return address.isPresent() ? address.get() : DEFAULT_ADDRESS;
    }

    public void setAddress(String address) {
        this.address = Optional.ofNullable(address);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
