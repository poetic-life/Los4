package com.laclippers.los.repository;

import com.laclippers.los.entity.Player;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findBySeason(String season, Sort sort);
    Optional<Player> findBySeasonAndExternalId(String season, String externalId);
}