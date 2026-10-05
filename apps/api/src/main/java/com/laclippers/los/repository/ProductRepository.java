package com.laclippers.los.repository;

import com.laclippers.los.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContaining(String keyword, Pageable pageable);

    Page<Product> findByCategoryIdAndNameContaining(Integer categoryId, String keyword, Pageable pageable);

    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);
}