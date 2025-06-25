package com.mysupermarket.supermarket.backend.service.member;

import com.mysupermarket.supermarket.backend.dto.MemberRegisterRequest;
import com.mysupermarket.supermarket.backend.dto.MemberLoginRequest;
import com.mysupermarket.supermarket.backend.dto.LoginResponseDTO;
import com.mysupermarket.supermarket.backend.entity.Member;
import com.mysupermarket.supermarket.backend.repository.MemberRepository;
import com.mysupermarket.supermarket.backend.repository.OrderRepository;
import com.mysupermarket.supermarket.backend.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    // 用于保存token和会员ID的映射
    private static final ConcurrentHashMap<String, Integer> tokenMemberMap = new ConcurrentHashMap<>();

    public String register(MemberRegisterRequest request) {
        // 检查手机号是否已注册
        if (memberRepository.findByPhoneNumber(request.getPhoneNumber()) != null) {
            return "手机号已注册";
        }
        // 创建新会员
        Member member = new Member();
        member.setPhoneNumber(request.getPhoneNumber());
        member.setPasswordHash(request.getPassword()); // 简单处理，实际可加密
        member.setName(request.getName());
        member.setMemberLevel("普通会员");
        member.setWalletBalance(BigDecimal.ZERO);
        member.setRebateBalance(BigDecimal.ZERO);
        member.setStatus(Member.STATUS_ACTIVE);
        memberRepository.save(member);
        return "注册成功";
    }

    public String login(MemberLoginRequest request) {
        Member member = memberRepository.findByPhoneNumber(request.getPhoneNumber());
        if (member == null) {
            return "手机号未注册";
        }
        if (!member.getPasswordHash().equals(request.getPassword())) {
            return "密码错误";
        }
        if (!Member.STATUS_ACTIVE.equals(member.getStatus())) {
            return "会员状态异常";
        }
        return "登录成功";
    }

    // 会员注销方法（不是logout）
    public boolean deactivateMemberById(Integer memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            return false;
        }
        member.setStatus(Member.STATUS_DEACTIVATED);
        memberRepository.save(member);
        return true;
    }

    // 新增：token登录
    public LoginResponseDTO loginWithToken(MemberLoginRequest request) {
        Member member = memberRepository.findByPhoneNumber(request.getPhoneNumber());
        if (member == null) {
            return new LoginResponseDTO(1, "手机号未注册", null);
        }
        if (!member.getPasswordHash().equals(request.getPassword())) {
            return new LoginResponseDTO(1, "密码错误", null);
        }
        if (!Member.STATUS_ACTIVE.equals(member.getStatus())) {
            return new LoginResponseDTO(1, "会员状态异常", null);
        }
        // 登录成功，生成token
        String token = UUID.randomUUID().toString().replace("-", "");
        tokenMemberMap.put(token, member.getMemberId());
        return new LoginResponseDTO(0, "登录成功", token);
    }

    // 校验token，返回会员ID，若无效返回null
    public Integer getMemberIdByToken(String token) {
        return tokenMemberMap.get(token);
    }

    // 根据会员ID获取会员信息
    public Member getMemberById(Integer memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    public java.util.List<Order> getOrdersByMemberId(Integer memberId) {
        return orderRepository.findByMemberId(memberId);
    }
}
