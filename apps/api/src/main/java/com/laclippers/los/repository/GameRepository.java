package com.laclippers.los.repository;

import com.laclippers.los.entity.Game;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findBySeason(String season, Sort sort);
    Optional<Game> findBySeasonAndExternalId(String season, String externalId);
}