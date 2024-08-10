package com.ll.sapp.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PostDto {
    private final long id;
    private final LocalDateTime createDate;
    private final LocalDateTime modifyDate;
    private final String title;
    private final String content;

    public static PostDto from(Post post) {
        return new PostDto(post.getId(), post.getCreateDate(), post.getModifyDate(), post.getTitle(), post.getContent());
    }
}
