package com.prash.fintrackapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prash.fintrackapi.model.Category;
import com.prash.fintrackapi.model.TransactionType;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    boolean existsByName(String name);
    List<Category> findByType(TransactionType type);
}