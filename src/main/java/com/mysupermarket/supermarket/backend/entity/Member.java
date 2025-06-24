package com.mysupermarket.supermarket.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Members")
public class Member {
    public static final String STATUS_ACTIVE = "活跃";
    public static final String STATUS_DEACTIVATED = "已注销";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memberId;

    @Column(nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Column(length = 100)
    private String name;

    @Column(length = 50)
    private String memberLevel;

    @Column(precision = 10, scale = 2)
    private BigDecimal walletBalance = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal rebateBalance = BigDecimal.ZERO;

    @Column(length = 20)
    private String status;

    public Member() {}

    // Getter 和 Setter
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMemberLevel() { return memberLevel; }
    public void setMemberLevel(String memberLevel) { this.memberLevel = memberLevel; }
    public BigDecimal getWalletBalance() { return walletBalance; }
    public void setWalletBalance(BigDecimal walletBalance) { this.walletBalance = walletBalance; }
    public BigDecimal getRebateBalance() { return rebateBalance; }
    public void setRebateBalance(BigDecimal rebateBalance) { this.rebateBalance = rebateBalance; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 