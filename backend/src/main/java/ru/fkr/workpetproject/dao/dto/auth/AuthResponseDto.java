package ru.fkr.workpetproject.dao.dto.auth;

import lombok.Getter;

@Getter
public class AuthResponseDto {
    private String token;
    private String username;
    private String role;

    public AuthResponseDto(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }
}
