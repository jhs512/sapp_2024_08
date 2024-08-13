package com.ll.sapp.member;

import java.time.LocalDateTime;

public record MemberDto(long id, LocalDateTime createDate, LocalDateTime modifyDate, String username) {
    public static MemberDto from(Member member) {
        return new MemberDto(member.getId(), member.getCreateDate(), member.getModifyDate(), member.getUsername());
    }
}
