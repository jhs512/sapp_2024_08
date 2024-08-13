package com.ll.sapp.post;

import com.ll.sapp.member.Member;
import com.ll.sapp.member.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostApiV1Controller {
    private final MemberService memberService;
    private final PostService postService;

    @GetMapping("")
    public List<PostDto> getPosts() {
        List<Post> posts = postService.findAllByOrderByIdDesc();

        return posts.stream()
                .map(PostDto::from)
                .toList();
    }

    @GetMapping("/{id}")
    public PostDto getPost(@PathVariable long id) {
        Post post = postService.findById(id).get();

        return PostDto.from(post);
    }


    public record PostWriteReqBody(
            @NotBlank String title,
            @NotBlank String content
    ) {
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<PostDto> writePost(
            @Valid @RequestBody PostApiV1Controller.PostWriteReqBody reqBody,
            Principal principal
    ) {
        Member member = memberService.findByUsername(principal.getName()).get();
        Post post = postService.write(member, reqBody.title, reqBody.content);
        return ResponseEntity
                .ok()
                .body(PostDto.from(post));
    }


    public record PostModifyReqBody(
            @NotBlank String title,
            @NotBlank String content
    ) {
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> modifyPost(
            @PathVariable long id,
            @Valid @RequestBody PostApiV1Controller.PostModifyReqBody reqBody,
            Principal principal
    ) {
        Member member = memberService.findByUsername(principal.getName()).get();

        Post post = postService.findById(id).get();

        if (!post.getAuthor().equals(member)) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }

        postService.modify(post, reqBody.title, reqBody.content);
        return ResponseEntity
                .ok()
                .body(PostDto.from(post));
    }
}
