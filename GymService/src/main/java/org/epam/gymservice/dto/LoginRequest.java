package org.epam.gymservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public static LoginRequest of(String username, String password){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        return loginRequest;
    }

}

