package com.mtons.mblog.web.controller.api;

import com.mtons.mblog.base.lang.Result;
import com.mtons.mblog.modules.data.UserVO;
import com.mtons.mblog.modules.service.UserFollowService;
import com.mtons.mblog.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserApiController extends BaseController {
    @Autowired
    private UserFollowService userFollowService;

    @PostMapping("/follow/{userId}")
    public Result<Map<String, Object>> toggleFollow(@PathVariable long userId) {
        if (!isAuthenticated()) {
            return Result.failure("请先登录");
        }
        long currentUserId = getProfile().getId();
        try {
            if (userFollowService.isFollowing(currentUserId, userId)) {
                userFollowService.unfollow(currentUserId, userId);
            } else {
                userFollowService.follow(currentUserId, userId);
            }
            Map<String, Object> data = new HashMap<>(3);
            data.put("following", userFollowService.isFollowing(currentUserId, userId));
            data.put("followers", userFollowService.countFollowers(userId));
            data.put("followings", userFollowService.countFollowing(userId));
            return Result.success(data);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @GetMapping("/user/follow")
    public Result<Page<UserVO>> followingList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        if (!isAuthenticated()) {
            return Result.failure("请先登录");
        }
        return Result.success(userFollowService.pagingFollowing(
                wrapPageable(pageNum, pageSize),
                getProfile().getId()));
    }

    @GetMapping("/user/fans")
    public Result<Page<UserVO>> fansList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        if (!isAuthenticated()) {
            return Result.failure("请先登录");
        }
        return Result.success(userFollowService.pagingFollowers(
                wrapPageable(pageNum, pageSize),
                getProfile().getId()));
    }
}
