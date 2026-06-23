package com.mtons.mblog.modules.repository;

import com.mtons.mblog.modules.entity.UserFollow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
    UserFollow findByFollowerIdAndFolloweeId(long followerId, long followeeId);

    long countByFolloweeId(long followeeId);

    long countByFollowerId(long followerId);

    Page<UserFollow> findAllByFollowerId(Pageable pageable, long followerId);

    Page<UserFollow> findAllByFolloweeId(Pageable pageable, long followeeId);
}
