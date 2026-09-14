package com.goldenboiz.repository;

import com.goldenboiz.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // NEW: for search
    List<Member> findByFullNameContainingIgnoreCaseOrPhoneContainingOrEmailContaining(String name, String phone, String email);
}