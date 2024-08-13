package com.ll.sapp.post;

import com.ll.sapp.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<Post> findAllByOrderByIdDesc() {
        return postRepository.findAllByOrderByIdDesc();
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    public Post write(Member author, String title, String content) {
        Post post = new Post();
        post.setAuthor(author);
        post.setTitle(title);
        post.setContent(content);
        postRepository.save(post);

        return post;
    }

    public long count() {
        return postRepository.count();
    }

    public void modify(Post post, String title, String content) {
        post.setTitle(title);
        post.setContent(content);
        postRepository.save(post);
    }
}
