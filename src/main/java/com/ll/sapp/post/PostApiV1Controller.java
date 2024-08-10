package com.ll.sapp.post;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostApiV1Controller {
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

    public record WritePostReqBody(
            @NotBlank String title,
            @NotBlank String content
    ) {
    }

    @PostMapping
    public ResponseEntity<PostDto> writePost(@Valid @RequestBody WritePostReqBody reqBody) {
        Post post = postService.write(reqBody.title, reqBody.content);
        return ResponseEntity
                .ok()
                .body(PostDto.from(post));
    }
}
