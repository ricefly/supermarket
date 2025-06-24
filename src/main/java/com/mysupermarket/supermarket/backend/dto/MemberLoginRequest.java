package com.mysupermarket.supermarket.backend.dto;

public class MemberLoginRequest {
    private String phoneNumber;
    private String password;

    // Getter 和 Setter
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
} 