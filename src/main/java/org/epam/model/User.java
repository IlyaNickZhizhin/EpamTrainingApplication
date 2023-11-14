package org.epam.model;

import lombok.*;
import org.epam.model.gymModel.Model;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class User implements Model {

    private int id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private boolean isActive;
}
