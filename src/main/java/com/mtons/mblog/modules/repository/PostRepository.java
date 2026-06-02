/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mtons.mblog.modules.repository;

import com.mtons.mblog.modules.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author langhsu
 */
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    /**
     * 查询指定用户
     *
     * @param pageable
     * @param authorId
     * @return
     */
    Page<Post> findAllByAuthorId(Pageable pageable, long authorId);

    @Query("select coalesce(max(weight), 0) from Post")
    int maxWeight();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Post set views = views + :increment where id = :id")
    void updateViews(@Param("id") long id, @Param("increment") int increment);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Post set favors = favors + :increment where id = :id")
    void updateFavors(@Param("id") long id, @Param("increment") int increment);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Post set likes = likes + :increment where id = :id")
    void updateLikes(@Param("id") long id, @Param("increment") int increment);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Post set comments = comments + :increment where id = :id")
    void updateComments(@Param("id") long id, @Param("increment") int increment);

    @Query("select coalesce(p.likes, 0) from Post p where p.id = :id")
    int findLikesById(@Param("id") long id);

    @Query("select coalesce(p.favors, 0) from Post p where p.id = :id")
    int findFavorsById(@Param("id") long id);

    long countByTitleStartingWith(String prefix);

    boolean existsByTitle(String title);

    java.util.List<Post> findByTitleStartingWith(String prefix);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Post p set p.likes = :likes, p.views = :views, p.favors = :favors, p.created = :created where p.id = :id")
    void updateDemoMetrics(@Param("id") long id, @Param("likes") int likes, @Param("views") int views,
                           @Param("favors") int favors, @Param("created") java.util.Date created);

    @Query(value = "SELECT YEAR(created) AS year, MONTH(created) AS month, COUNT(*) AS count " +
            "FROM mto_post WHERE status = 0 GROUP BY YEAR(created), MONTH(created) " +
            "ORDER BY year DESC, month DESC", nativeQuery = true)
    List<Object[]> findArchiveStats();

}
