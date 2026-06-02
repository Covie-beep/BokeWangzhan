package com.mtons.mblog.web.controller.api;

import com.mtons.mblog.base.lang.Consts;
import com.mtons.mblog.base.lang.Result;
import com.mtons.mblog.base.utils.BeanMapUtils;
import com.mtons.mblog.modules.data.ArchiveVO;
import com.mtons.mblog.modules.data.CommentVO;
import com.mtons.mblog.modules.data.PostVO;
import com.mtons.mblog.modules.data.TagVO;
import com.mtons.mblog.modules.entity.Channel;
import com.mtons.mblog.modules.repository.PostRepository;
import com.mtons.mblog.modules.service.*;
import com.mtons.mblog.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ArticleApiController extends BaseController {
    @Autowired
    private PostService postService;
    @Autowired
    private PostSearchService postSearchService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostLikeService postLikeService;
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/article/list")
    public Result<Page<PostVO>> articleList(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String tagId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false, defaultValue = "newest") String order) {
        Page<PostVO> page;
        if (StringUtils.isNotBlank(tagId)) {
            page = postService.pagingByTag(wrapApiPageable(Sort.by(Sort.Direction.DESC, "created")), tagId);
        } else if (year != null && month != null) {
            page = postService.pagingByArchive(wrapApiPageable(Sort.by(Sort.Direction.DESC, "created")), year, month);
        } else {
            int channelId = categoryId == null ? 0 : categoryId;
            java.util.Set<Integer> excludeChannelIds = new java.util.HashSet<>();
            if (channelId <= 0) {
                channelService.findAll(Consts.STATUS_CLOSED).forEach(c -> excludeChannelIds.add(c.getId()));
            }
            page = postService.paging(
                    wrapApiPageable(Sort.by(Sort.Direction.DESC, BeanMapUtils.postOrder(order))),
                    channelId,
                    excludeChannelIds);
        }
        return Result.success(page);
    }

    @GetMapping("/article/{id}")
    public Result<PostVO> articleDetail(@PathVariable long id) {
        PostVO post = postService.get(id);
        if (post == null) {
            return Result.failure("文章不存在");
        }
        return Result.success(post);
    }

    @GetMapping("/article/hot")
    public Result<List<PostVO>> hotArticles(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(postService.findHottestPosts(limit));
    }

    @GetMapping("/article/search")
    public Result<Page<PostVO>> searchArticles(@RequestParam String keyword) {
        try {
            return Result.success(postSearchService.search(wrapApiPageable(), keyword));
        } catch (Exception e) {
            return Result.failure("搜索失败");
        }
    }

    @GetMapping("/category/list")
    public Result<List<Channel>> categoryList() {
        return Result.success(channelService.findAll(Consts.STATUS_NORMAL));
    }

    @GetMapping("/tag/list")
    public Result<Page<TagVO>> tagList() {
        return Result.success(tagService.pagingQueryTags(wrapApiPageable()));
    }

    @GetMapping("/archive/list")
    public Result<List<ArchiveVO>> archiveList() {
        return Result.success(postService.findArchives());
    }

    @GetMapping("/comment/list")
    public Result<Page<CommentVO>> commentList(
            @RequestParam long articleId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(commentService.pagingByPostId(
                wrapPageable(pageNum, pageSize),
                articleId));
    }

    @PostMapping("/like/{articleId}")
    public Result<Map<String, Object>> toggleLike(@PathVariable long articleId) {
        if (!isAuthenticated()) {
            return Result.failure("请先登录");
        }
        long userId = getProfile().getId();
        try {
            if (postLikeService.isLiked(userId, articleId)) {
                postService.unlike(userId, articleId);
            } else {
                postService.like(userId, articleId);
            }
            Map<String, Object> data = new HashMap<>(2);
            data.put("liked", postLikeService.isLiked(userId, articleId));
            data.put("likes", postRepository.findLikesById(articleId));
            return Result.success(data);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }
}
