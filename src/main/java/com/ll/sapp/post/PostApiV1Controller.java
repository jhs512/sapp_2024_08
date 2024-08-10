package com.ll.sapp.post;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
