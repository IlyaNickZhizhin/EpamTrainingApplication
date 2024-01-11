package org.epam.gymservice.dto;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponse {
    private String username;
    private String password;
}
