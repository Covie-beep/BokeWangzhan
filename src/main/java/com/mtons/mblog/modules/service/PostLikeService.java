package com.mtons.mblog.modules.service;

public interface PostLikeService {
    void like(long userId, long postId);

    void unlike(long userId, long postId);

    boolean isLiked(long userId, long postId);

    void deleteByPostId(long postId);
}
