package org.epam.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeLoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
    public LoginRequest getLogin(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(oldPassword);
        return loginRequest;
    }
}
