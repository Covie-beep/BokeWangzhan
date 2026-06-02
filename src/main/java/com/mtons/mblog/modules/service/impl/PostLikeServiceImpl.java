package com.mtons.mblog.modules.service.impl;

import com.mtons.mblog.modules.entity.PostLike;
import com.mtons.mblog.modules.repository.PostLikeRepository;
import com.mtons.mblog.modules.service.PostLikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PostLikeServiceImpl implements PostLikeService {
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void like(long userId, long postId) {
        PostLike existing = postLikeRepository.findByUserIdAndPostId(userId, postId);
        Assert.isNull(existing, "您已经点赞过此文章");

        PostLike record = new PostLike();
        record.setUserId(userId);
        record.setPostId(postId);
        record.setCreated(new Date());
        postLikeRepository.save(record);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void unlike(long userId, long postId) {
        PostLike existing = postLikeRepository.findByUserIdAndPostId(userId, postId);
        Assert.notNull(existing, "您还没有点赞此文章");
        postLikeRepository.delete(existing);
    }

    @Override
    public boolean isLiked(long userId, long postId) {
        return postLikeRepository.findByUserIdAndPostId(userId, postId) != null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteByPostId(long postId) {
        int rows = postLikeRepository.deleteByPostId(postId);
        log.info("postLikeRepository delete {}", rows);
    }
}
