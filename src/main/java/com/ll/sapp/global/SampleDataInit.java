package com.ll.sapp.global;

import com.ll.sapp.member.Member;
import com.ll.sapp.member.MemberService;
import com.ll.sapp.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class SampleDataInit {
    private final MemberService memberService;
    private final PostService postService;

    @Autowired
    @Lazy
    private SampleDataInit self;

    @Bean
    public ApplicationRunner sampleDataInitApplicationRunner() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        if (memberService.count() > 0) return;

        Member memberSystem = memberService.join("system", "1234");
        Member memberAdmin = memberService.join("admin", "1234");
        Member memberUser1 = memberService.join("user1", "1234");
        Member memberUser2 = memberService.join("user2", "1234");

        postService.write(memberUser1, "제목 1", "내용 1");
        postService.write(memberUser1, "제목 2", "내용 2");
        postService.write(memberUser2, "제목 3", "내용 3");
    }
}
