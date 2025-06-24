package com.mysupermarket.supermarket.backend.repository;

import com.mysupermarket.supermarket.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findByPhoneNumber(String phoneNumber);
}