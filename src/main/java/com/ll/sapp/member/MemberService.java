package com.ll.sapp.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AuthTokenService authTokenService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Member join(String username, String password) {
        Member member = new Member();
        member.setUsername(username);
        member.setPassword(passwordEncoder.encode(password));
        member.setRefreshToken(authTokenService.genRefreshToken());
        memberRepository.save(member);

        return member;
    }

    public long count() {
        return memberRepository.count();
    }

    public boolean checkPassword(Member member, String password) {
        return passwordEncoder.matches(password, member.getPassword());
    }

    public List<Member> findAllByOrderByIdDesc() {
        return memberRepository.findAllByOrderByIdDesc();
    }
}
