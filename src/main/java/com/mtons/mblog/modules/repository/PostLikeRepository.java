package com.mtons.mblog.modules.repository;

import com.mtons.mblog.modules.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    PostLike findByUserIdAndPostId(long userId, long postId);

    int deleteByPostId(long postId);
}
