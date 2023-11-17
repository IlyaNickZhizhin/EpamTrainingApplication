package org.epam.dto;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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

    private Date dateOfBirth;

    private String address;

    private boolean isActive;

}
