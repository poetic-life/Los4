package com.laclippers.los.repository;

import com.laclippers.los.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Long> {
    List<Faq> findByCategory(String category);
}