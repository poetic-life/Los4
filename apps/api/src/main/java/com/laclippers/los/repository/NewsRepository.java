package com.laclippers.los.repository;

import com.laclippers.los.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
    boolean existsBySourceUrl(String sourceUrl);

    Page<News> findByTitleContaining(String keyword, Pageable pageable);

    Page<News> findByCategory(String category, Pageable pageable);

    Page<News> findByCategoryAndTitleContaining(String category, String keyword, Pageable pageable);

    Page<News> findBySeason(String season, Pageable pageable);

    Page<News> findBySeasonAndCategory(String season, String category, Pageable pageable);

    Page<News> findBySeasonAndTitleContaining(String season, String keyword, Pageable pageable);

    Page<News> findBySeasonAndCategoryAndTitleContaining(String season, String category, String keyword, Pageable pageable);
}