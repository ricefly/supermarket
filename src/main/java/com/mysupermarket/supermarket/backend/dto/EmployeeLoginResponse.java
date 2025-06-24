package com.mysupermarket.supermarket.backend.dto;

public class EmployeeLoginResponse {
    private int code;
    private String msg;
    private String token;
    private String role;

    public EmployeeLoginResponse(int code, String msg, String token, String role) {
        this.code = code;
        this.msg = msg;
        this.token = token;
        this.role = role;
    }

    // Getter 和 Setter
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
} 