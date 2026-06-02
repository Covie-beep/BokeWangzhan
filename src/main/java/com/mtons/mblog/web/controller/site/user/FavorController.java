package com.mtons.mblog.web.controller.site.user;

import com.mtons.mblog.base.lang.Result;
import com.mtons.mblog.base.lang.Consts;
import com.mtons.mblog.modules.data.AccountProfile;
import com.mtons.mblog.modules.event.MessageEvent;
import com.mtons.mblog.modules.repository.PostRepository;
import com.mtons.mblog.modules.service.FavoriteService;
import com.mtons.mblog.modules.service.PostService;
import com.mtons.mblog.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author langhsu
 */
@RestController
@RequestMapping("/user")
public class FavorController extends BaseController {
    @Autowired
    private PostService postService;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("/favor")
    public Result<Map<String, Object>> favor(Long id) {
        if (id == null) {
            return Result.failure("操作失败");
        }
        try {
            AccountProfile up = getProfile();
            postService.favor(up.getId(), id);
            sendMessage(up.getId(), id);
            return Result.success(buildFavorResult(up.getId(), id));
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @RequestMapping("/unfavor")
    public Result<Map<String, Object>> unfavor(Long id) {
        if (id == null) {
            return Result.failure("操作失败");
        }
        try {
            AccountProfile up = getProfile();
            postService.unfavor(up.getId(), id);
            return Result.success(buildFavorResult(up.getId(), id));
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @RequestMapping("/favor/status")
    public Result<Map<String, Object>> status(Long id) {
        if (id == null) {
            return Result.failure("操作失败");
        }
        long userId = isAuthenticated() ? getProfile().getId() : 0;
        return Result.success(buildFavorResult(userId, id));
    }

    private Map<String, Object> buildFavorResult(long userId, long postId) {
        Map<String, Object> data = new HashMap<>(2);
        data.put("favored", userId > 0 && favoriteService.isFavorited(userId, postId));
        data.put("favors", postRepository.findFavorsById(postId));
        return data;
    }

    private void sendMessage(long userId, long postId) {
        MessageEvent event = new MessageEvent("MessageEvent" + System.currentTimeMillis());
        event.setFromUserId(userId);
        event.setEvent(Consts.MESSAGE_EVENT_FAVOR_POST);
        event.setPostId(postId);
        applicationContext.publishEvent(event);
    }
}
