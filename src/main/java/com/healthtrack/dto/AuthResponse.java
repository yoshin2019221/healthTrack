package com.healthtrack.dto;

public class AuthResponse {
    private String token;
    private String email;
    private String name;
    private Long userId;

    public AuthResponse(String token, String email, String name, Long userId) {
        this.token = token;
        this.email = email;
        this.name = name;
        this.userId = userId;
    }

    public String getToken() { return token; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public Long getUserId() { return userId; }
}
