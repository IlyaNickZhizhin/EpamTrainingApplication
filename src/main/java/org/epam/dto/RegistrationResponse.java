package org.epam.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class RegistrationResponse {
    private String username;
    private String password;
}
