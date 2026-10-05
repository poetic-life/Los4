package com.laclippers.los.repository;

import com.laclippers.los.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeasonRepository extends JpaRepository<Season, Long> {
    boolean existsBySeason(String season);
    Optional<Season> findBySeason(String season);
}