package org.epam.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class User {

    private int id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private boolean isActive;
}
