package com.laclippers.los.repository;

import com.laclippers.los.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserId(Long userId);

    @Query("select coalesce(sum(p.likes), 0) from Post p where p.userId = :userId")
    long sumLikesByUserId(@Param("userId") Long userId);
}