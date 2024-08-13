package com.ll.sapp.member;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberApiV1Controller {
    private final MemberService memberService;
    private final AuthTokenService authTokenService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<MemberDto> getMembers() {
        List<Member> members = memberService.findAllByOrderByIdDesc();

        return members.stream()
                .map(MemberDto::from)
                .toList();
    }

    public record MemberLoginReqBody(
            @NotBlank String username,
            @NotBlank String password
    ) {
    }

    @PostMapping("/login")
    public ResponseEntity<MemberDto> login(
            @Valid @RequestBody MemberLoginReqBody reqBody
    ) {
        Member member = memberService.findByUsername(reqBody.username).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!memberService.checkPassword(member, reqBody.password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = authTokenService.genAccessToken(member);
        String refreshToken = member.getRefreshToken();

        return ResponseEntity
                .ok()
                .header("Authorization", accessToken)
                .header("Refresh-Token", refreshToken)
                .body(MemberDto.from(member));
    }

    public record MemberJoinReqBody(
            @NotBlank String username,
            @NotBlank String password
    ) {
    }

    @PostMapping("")
    public ResponseEntity<MemberDto> join(
            @Valid @RequestBody MemberJoinReqBody reqBody
    ) {
        memberService.findByUsername(reqBody.username)
                .ifPresent(member -> {
                    throw new RuntimeException("이미 존재하는 사용자입니다.");
                });

        Member member = memberService.join(reqBody.username, reqBody.password);

        return ResponseEntity
                .ok()
                .body(MemberDto.from(member));
    }
}
