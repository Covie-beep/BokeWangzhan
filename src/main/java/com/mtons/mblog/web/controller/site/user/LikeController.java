package com.mtons.mblog.web.controller.site.user;

import com.mtons.mblog.base.lang.Consts;
import com.mtons.mblog.base.lang.Result;
import com.mtons.mblog.modules.data.AccountProfile;
import com.mtons.mblog.modules.event.MessageEvent;
import com.mtons.mblog.modules.repository.PostRepository;
import com.mtons.mblog.modules.service.PostLikeService;
import com.mtons.mblog.modules.service.PostService;
import com.mtons.mblog.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class LikeController extends BaseController {
    @Autowired
    private PostService postService;
    @Autowired
    private PostLikeService postLikeService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("/like")
    public Result<Map<String, Object>> like(Long id) {
        if (id == null) {
            return Result.failure("操作失败");
        }
        try {
            AccountProfile profile = getProfile();
            postService.like(profile.getId(), id);
            sendMessage(profile.getId(), id);
            return Result.success(buildLikeResult(profile.getId(), id));
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @RequestMapping("/unlike")
    public Result<Map<String, Object>> unlike(Long id) {
        if (id == null) {
            return Result.failure("操作失败");
        }
        try {
            AccountProfile profile = getProfile();
            postService.unlike(profile.getId(), id);
            return Result.success(buildLikeResult(profile.getId(), id));
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @RequestMapping("/like/status")
    public Result<Map<String, Object>> status(Long id) {
        if (id == null) {
            return Result.failure("操作失败");
        }
        long userId = isAuthenticated() ? getProfile().getId() : 0;
        return Result.success(buildLikeResult(userId, id));
    }

    private Map<String, Object> buildLikeResult(long userId, long postId) {
        Map<String, Object> data = new HashMap<>(2);
        data.put("liked", userId > 0 && postLikeService.isLiked(userId, postId));
        data.put("likes", postRepository.findLikesById(postId));
        return data;
    }

    private void sendMessage(long userId, long postId) {
        MessageEvent event = new MessageEvent("MessageEvent" + System.currentTimeMillis());
        event.setFromUserId(userId);
        event.setEvent(Consts.MESSAGE_EVENT_LIKE_POST);
        event.setPostId(postId);
        applicationContext.publishEvent(event);
    }
}
