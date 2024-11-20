package com.fpoly.java5.jpa;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fpoly.java5.entities.Category;

public interface CategoryJPA extends JpaRepository<Category, Integer> {
    @Query("SELECT cat FROM Category cat WHERE cat.active=true")
    List<Category> getAllCategoryByActive();

    @Query("SELECT cat FROM Category cat ORDER BY cat.id DESC")
    Page<Category> pageAllCategory(Pageable pageable);

    @Query("SELECT cat FROM Category cat " +
            "WHERE (:keyword IS NULL OR cat.name LIKE %:keyword%) " +
            "AND (:status IS NULL OR cat.active = :status) ORDER BY cat.id DESC")
    Page<Category> filterCategoryByNameAndActive(
            @Param("keyword") String keyword,
            @Param("status") Boolean status,
            Pageable pageable);

            @Query("SELECT cat FROM Category cat WHERE cat.name = ?2 AND (cat.id <> ?1 OR ?1 IS NULL)")
            Optional<Category> findByNameExist(Integer id, String name);
            


} 
