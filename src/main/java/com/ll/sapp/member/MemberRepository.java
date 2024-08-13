package com.ll.sapp.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    List<Member> findAllByOrderByIdDesc();

    Optional<Member> findByRefreshToken(String refreshToken);
}
