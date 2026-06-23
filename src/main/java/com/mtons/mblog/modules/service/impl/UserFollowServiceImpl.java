package com.mtons.mblog.modules.service.impl;

import com.mtons.mblog.modules.data.UserVO;
import com.mtons.mblog.modules.entity.UserFollow;
import com.mtons.mblog.modules.repository.UserFollowRepository;
import com.mtons.mblog.modules.service.UserFollowService;
import com.mtons.mblog.modules.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserFollowServiceImpl implements UserFollowService {
    @Autowired
    private UserFollowRepository userFollowRepository;
    @Autowired
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void follow(long followerId, long followeeId) {
        Assert.isTrue(followerId > 0 && followeeId > 0, "用户不存在");
        Assert.isTrue(followerId != followeeId, "不能关注自己");
        Assert.notNull(userService.get(followeeId), "该用户不存在");
        UserFollow existing = userFollowRepository.findByFollowerIdAndFolloweeId(followerId, followeeId);
        Assert.isNull(existing, "您已经关注过该用户");

        UserFollow record = new UserFollow();
        record.setFollowerId(followerId);
        record.setFolloweeId(followeeId);
        record.setCreated(new Date());
        userFollowRepository.save(record);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void unfollow(long followerId, long followeeId) {
        UserFollow existing = userFollowRepository.findByFollowerIdAndFolloweeId(followerId, followeeId);
        Assert.notNull(existing, "您还没有关注该用户");
        userFollowRepository.delete(existing);
    }

    @Override
    public boolean isFollowing(long followerId, long followeeId) {
        if (followerId <= 0 || followeeId <= 0) {
            return false;
        }
        return userFollowRepository.findByFollowerIdAndFolloweeId(followerId, followeeId) != null;
    }

    @Override
    public long countFollowers(long userId) {
        return userFollowRepository.countByFolloweeId(userId);
    }

    @Override
    public long countFollowing(long userId) {
        return userFollowRepository.countByFollowerId(userId);
    }

    @Override
    public Page<UserVO> pagingFollowers(Pageable pageable, long userId) {
        Page<UserFollow> page = userFollowRepository.findAllByFolloweeId(pageable, userId);
        return toUserPage(pageable, page, true);
    }

    @Override
    public Page<UserVO> pagingFollowing(Pageable pageable, long userId) {
        Page<UserFollow> page = userFollowRepository.findAllByFollowerId(pageable, userId);
        return toUserPage(pageable, page, false);
    }

    private Page<UserVO> toUserPage(Pageable pageable, Page<UserFollow> page, boolean followers) {
        List<UserVO> users = new ArrayList<>();
        for (UserFollow item : page.getContent()) {
            long targetId = followers ? item.getFollowerId() : item.getFolloweeId();
            UserVO user = userService.get(targetId);
            if (user != null) {
                users.add(user);
            }
        }
        return new PageImpl<>(users, pageable, page.getTotalElements());
    }
}
