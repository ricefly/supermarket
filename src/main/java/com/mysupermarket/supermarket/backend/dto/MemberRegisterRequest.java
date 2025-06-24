package com.mysupermarket.supermarket.backend.dto;

public class MemberRegisterRequest {
    private String phoneNumber;
    private String password;
    private String name;

    // Getter 和 Setter
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
} 