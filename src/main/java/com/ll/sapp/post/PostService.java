package com.ll.sapp.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<Post> findAllByOrderByIdDesc() {
        return postRepository.findAllByOrderByIdDesc();
    }

    public Post write(String title, String content) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        postRepository.save(post);

        return post;
    }

    public long count() {
        return postRepository.count();
    }
}
