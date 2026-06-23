package com.mtons.mblog.web.controller.site.user;

import com.mtons.mblog.base.lang.Consts;
import com.mtons.mblog.base.lang.Result;
import com.mtons.mblog.modules.data.AccountProfile;
import com.mtons.mblog.modules.event.MessageEvent;
import com.mtons.mblog.modules.service.UserFollowService;
import com.mtons.mblog.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class FollowController extends BaseController {
    @Autowired
    private UserFollowService userFollowService;
    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("/follow")
    public Result<Map<String, Object>> follow(Long userId) {
        if (userId == null) {
            return Result.failure("操作失败");
        }
        try {
            AccountProfile profile = getProfile();
            userFollowService.follow(profile.getId(), userId);
            sendMessage(profile.getId(), userId);
            return Result.success(buildFollowResult(profile.getId(), userId));
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @RequestMapping("/unfollow")
    public Result<Map<String, Object>> unfollow(Long userId) {
        if (userId == null) {
            return Result.failure("操作失败");
        }
        try {
            AccountProfile profile = getProfile();
            userFollowService.unfollow(profile.getId(), userId);
            return Result.success(buildFollowResult(profile.getId(), userId));
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @RequestMapping("/follow/status")
    public Result<Map<String, Object>> status(Long userId) {
        if (userId == null) {
            return Result.failure("操作失败");
        }
        long currentUserId = isAuthenticated() ? getProfile().getId() : 0;
        return Result.success(buildFollowResult(currentUserId, userId));
    }

    private Map<String, Object> buildFollowResult(long followerId, long followeeId) {
        Map<String, Object> data = new HashMap<>(3);
        data.put("following", followerId > 0 && userFollowService.isFollowing(followerId, followeeId));
        data.put("followers", userFollowService.countFollowers(followeeId));
        data.put("followings", userFollowService.countFollowing(followeeId));
        return data;
    }

    private void sendMessage(long fromUserId, long toUserId) {
        MessageEvent event = new MessageEvent("MessageEvent" + System.currentTimeMillis());
        event.setFromUserId(fromUserId);
        event.setToUserId(toUserId);
        event.setEvent(Consts.MESSAGE_EVENT_FOLLOW_USER);
        applicationContext.publishEvent(event);
    }
}
