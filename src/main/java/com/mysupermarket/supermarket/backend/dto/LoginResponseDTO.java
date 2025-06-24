package com.mysupermarket.supermarket.backend.dto;

public class LoginResponseDTO {
    private int code; // 0成功，1失败
    private String msg;
    private String token;

    public LoginResponseDTO() {}
    public LoginResponseDTO(int code, String msg, String token) {
        this.code = code;
        this.msg = msg;
        this.token = token;
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
