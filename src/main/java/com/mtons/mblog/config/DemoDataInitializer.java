package com.mtons.mblog.config;

import com.mtons.mblog.base.lang.Consts;
import com.mtons.mblog.modules.data.PostVO;
import com.mtons.mblog.modules.entity.Post;
import com.mtons.mblog.modules.repository.PostRepository;
import com.mtons.mblog.modules.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * H2 本地开发时自动填充演示文章，便于体验排序、热门、归档等功能。
 */
@Slf4j
@Order(3)
@Component
@Profile("h2")
public class DemoDataInitializer implements ApplicationRunner {
    private static final String LEGACY_TITLE_PREFIX = "【示例】";
    private static final String SEED_MARKER_TITLE = "Spring Boot 入门实战指南";

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void run(ApplicationArguments args) {
        removeLegacyTitlePrefix();

        if (postRepository.existsByTitle(SEED_MARKER_TITLE)) {
            return;
        }

        log.info("Seeding demo posts for local development...");
        Object[][] samples = {
                {SEED_MARKER_TITLE, "博客", "Java,Spring", 156, 3280, 42, -1, 3},
                {"MySQL 索引优化十条经验", "博客", "MySQL,数据库", 128, 5120, 35, -2, 8},
                {"前后端分离项目部署笔记", "博客", "Vue,DevOps", 96, 2890, 28, -3, 15},
                {"RESTful API 设计规范整理", "博客", "API,后端", 88, 4100, 31, -1, 22},
                {"Markdown 写作与排版技巧", "随笔", "写作,Markdown", 72, 1960, 19, -2, 5},
                {"个人博客主题配色分享", "随笔", "前端,CSS", 64, 1750, 16, -4, 12},
                {"Java 并发编程学习路线", "博客", "Java,并发", 58, 3680, 24, -5, 18},
                {"Docker Compose 一键部署", "分享", "Docker,运维", 52, 2450, 21, -6, 9},
                {"Git 工作流与分支策略", "分享", "Git,团队协作", 47, 2210, 17, -2, 27},
                {"单元测试最佳实践", "博客", "测试,Java", 41, 1890, 14, -7, 14},
                {"Freemarker 模板开发笔记", "博客", "模板,前端", 36, 1540, 12, -3, 30},
                {"归档功能与按月浏览体验", "随笔", "博客,归档", 29, 980, 9, -8, 6},
        };

        Calendar calendar = Calendar.getInstance();
        for (Object[] sample : samples) {
            String title = String.valueOf(sample[0]);
            PostVO post = new PostVO();
            post.setTitle(title);
            post.setSummary("一篇关于「" + title + "」的技术分享，可直接体验最新、热门、最多赞等排序效果。");
            post.setContent("## " + title + "\n\n"
                    + "本文用于体验博客系统的列表排序、热门推荐、点赞收藏与归档等功能。\n\n"
                    + "- 支持按最新、阅读量、点赞数排序\n"
                    + "- 支持标签与频道分类\n"
                    + "- 支持文章详情页点赞/收藏\n\n"
                    + "欢迎继续发布自己的内容进行测试。");
            post.setEditor("markdown");
            post.setChannelId(channelId(String.valueOf(sample[1])));
            post.setTags(String.valueOf(sample[2]));
            post.setAuthorId(1L);
            post.setStatus(Consts.STATUS_NORMAL);

            long id = postService.post(post);

            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, (Integer) sample[6]);
            calendar.set(Calendar.HOUR_OF_DAY, (Integer) sample[7]);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            postRepository.updateDemoMetrics(
                    id,
                    (Integer) sample[3],
                    (Integer) sample[4],
                    (Integer) sample[5],
                    calendar.getTime());
        }

        log.info("Demo posts seeded: {}", samples.length);
    }

    private void removeLegacyTitlePrefix() {
        List<Post> legacyPosts = postRepository.findByTitleStartingWith(LEGACY_TITLE_PREFIX);
        for (Post post : legacyPosts) {
            post.setTitle(post.getTitle().substring(LEGACY_TITLE_PREFIX.length()));
            postRepository.save(post);
        }
        if (!legacyPosts.isEmpty()) {
            log.info("Removed legacy title prefix from {} posts", legacyPosts.size());
        }
    }

    private int channelId(String name) {
        switch (name) {
            case "随笔":
                return 3;
            case "分享":
                return 4;
            default:
                return 2;
        }
    }
}
