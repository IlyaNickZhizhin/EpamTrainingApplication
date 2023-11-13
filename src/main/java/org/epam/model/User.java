package org.epam.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class User {

    private int id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private boolean isActive;
}
