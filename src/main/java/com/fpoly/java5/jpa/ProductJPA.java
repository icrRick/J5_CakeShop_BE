package com.fpoly.java5.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import com.fpoly.java5.entities.Product;

public interface ProductJPA extends JpaRepository<Product, Integer> {
        @Query("SELECT p FROM Product p WHERE p.active=true ORDER BY p.id DESC")
        Page<Product> getPageAllProductOrderById(Pageable pageable);

        @Query("SELECT p FROM Product p WHERE p.active = ?1")
        List<Product> findAllWithActive(boolean active);

       @Query("SELECT p FROM Product p WHERE p.name = ?2 AND (p.id <> ?1 OR ?1 IS NULL)")
            Optional<Product> findByNameExist(Integer id, String name);

        @Query("SELECT p FROM Product p WHERE " +
                        "(p.name LIKE CONCAT('%', :keyword, '%') OR :keyword IS NULL) " +
                        "AND (p.category.id = :categoryId OR :categoryId IS NULL) " +
                        "AND (p.price >= COALESCE(:minPrice, 0)) " +
                        "AND (p.price <= COALESCE(:maxPrice, p.price)) " +
                        "AND p.active = true")
        Page<Product> getFilterProductByActive(@Param("keyword") String keyword,
                        @Param("categoryId") Integer categoryId,
                        @Param("minPrice") Double minPrice,
                        @Param("maxPrice") Double maxPrice,
                        Pageable pageable);

        @Query("SELECT p FROM Product p ORDER BY p.id DESC")
        Page<Product> pageAllProduct(Pageable pageable);

        @Query("SELECT p FROM Product p WHERE(:keyword is null OR p.name Like %:keyword% OR p.flavor Like %:keyword% OR p.ingredients Like %:keyword%) AND (:status IS NULL OR p.active = :status)")
        Page<Product> pageAllFilterProduct(@Param("keyword") String keyword, @Param("status") Boolean status,
                        Pageable pageable);

        @Transactional(readOnly = true)
        @Procedure(procedureName = "GetCategoryProductSummary")
        List<Object[]> getCategoryProductSummary();
        @Modifying
        @Transactional
        @Query("UPDATE Product p SET p.active = ?2 WHERE p.id = ?1")
        int softRemoveProductByProductId(int id, boolean active);

}
