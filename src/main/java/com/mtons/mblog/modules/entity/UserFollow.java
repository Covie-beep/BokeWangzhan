package com.mtons.mblog.modules.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户关注关系
 */
@Entity
@Table(name = "mto_user_follow", indexes = {
        @Index(name = "UK_FOLLOWER_FOLLOWEE", columnList = "follower_id,followee_id", unique = true),
        @Index(name = "IK_FOLLOWEE_ID", columnList = "followee_id")
})
public class UserFollow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "follower_id")
    private long followerId;

    @Column(name = "followee_id")
    private long followeeId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(long followerId) {
        this.followerId = followerId;
    }

    public long getFolloweeId() {
        return followeeId;
    }

    public void setFolloweeId(long followeeId) {
        this.followeeId = followeeId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
