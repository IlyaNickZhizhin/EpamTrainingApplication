package org.epam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainerProfile {
    private int id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private String specialization;

    private boolean isActive;
}
