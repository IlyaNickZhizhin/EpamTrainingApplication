package org.epam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TraineeProfile {

    private int id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private LocalDate dateOfBirth;

    private String address;

    private boolean isActive;

}
