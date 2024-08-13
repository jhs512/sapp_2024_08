package com.ll.sapp.member;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberApiV1Controller {
    private final MemberService memberService;
    private final AuthTokenService authTokenService;

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

        return ResponseEntity
                .ok()
                .header("Authorization", accessToken)
                .body(MemberDto.from(member));
    }
}
