package com.mtons.mblog.modules.service;

import com.mtons.mblog.modules.data.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserFollowService {
    void follow(long followerId, long followeeId);

    void unfollow(long followerId, long followeeId);

    boolean isFollowing(long followerId, long followeeId);

    long countFollowers(long userId);

    long countFollowing(long userId);

    Page<UserVO> pagingFollowers(Pageable pageable, long userId);

    Page<UserVO> pagingFollowing(Pageable pageable, long userId);
}
