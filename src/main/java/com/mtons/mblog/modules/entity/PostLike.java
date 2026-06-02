package com.mtons.mblog.modules.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 文章点赞记录
 */
@Entity
@Table(name = "mto_post_like", indexes = {
        @Index(name = "UK_USER_POST", columnList = "user_id,post_id", unique = true),
        @Index(name = "IK_POST_ID", columnList = "post_id")
})
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "post_id")
    private long postId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
