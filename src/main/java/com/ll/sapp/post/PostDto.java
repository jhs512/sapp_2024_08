package com.ll.sapp.post;

import java.time.LocalDateTime;

public record PostDto(long id, LocalDateTime createDate, LocalDateTime modifyDate, String title, String content) {
    public static PostDto from(Post post) {
        return new PostDto(post.getId(), post.getCreateDate(), post.getModifyDate(), post.getTitle(), post.getContent());
    }
}
