package com.mysupermarket.supermarket.backend.controller;

import com.mysupermarket.supermarket.backend.dto.MemberRegisterRequest;
import com.mysupermarket.supermarket.backend.dto.MemberLoginRequest;
import com.mysupermarket.supermarket.backend.dto.LoginResponseDTO;
import com.mysupermarket.supermarket.backend.entity.Member;
import com.mysupermarket.supermarket.backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    public String register(@RequestBody MemberRegisterRequest request) {
        return memberService.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody MemberLoginRequest request) {
        return memberService.login(request);
    }

    // 新增：token登录接口
    @PostMapping("/loginWithToken")
    public LoginResponseDTO loginWithToken(@RequestBody MemberLoginRequest request) {
        return memberService.loginWithToken(request);
    }

    // 会员注销接口（函数名不是logout）
    @PostMapping("/deactivate")
    public String deactivateMember(@RequestHeader("token") String token) {
        Integer memberId = memberService.getMemberIdByToken(token);
        if (memberId == null) {
            return "未登录或token无效";
        }
        boolean result = memberService.deactivateMemberById(memberId);
        return result ? "会员已注销" : "注销失败";
    }

    // 新增：带token的会员信息查询接口
    @GetMapping("/info")
    public Object getMemberInfo(@RequestHeader("token") String token) {
        Integer memberId = memberService.getMemberIdByToken(token);
        if (memberId == null) {
            return "未登录或token无效";
        }
        Member member = memberService.getMemberById(memberId);
        if (member == null) {
            return "会员不存在";
        }
        Map<String, Object> info = new HashMap<>();
        info.put("phoneNumber", member.getPhoneNumber());
        info.put("name", member.getName());
        info.put("memberLevel", member.getMemberLevel());
        info.put("walletBalance", member.getWalletBalance());
        info.put("rebateBalance", member.getRebateBalance());
        info.put("status", member.getStatus());
        return info;
    }
}
