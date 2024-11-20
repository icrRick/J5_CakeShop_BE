package com.fpoly.java5.entities;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name", columnDefinition = "NVARCHAR(100)")
    private String name;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private Double price;
    @Column(name = "flavor", columnDefinition = "NVARCHAR(255)")
    private String flavor;
    @Column(name = "descriptions", columnDefinition = "NVARCHAR(255)")
    private String descriptions;
    @Column(name = "ingredients", columnDefinition = "NVARCHAR(255)")
    private String ingredients;
    @Column(name = "active")
    private Boolean active;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<Image> images;

    @OneToMany(mappedBy = "product")
    private List<Comment> comments;

    public Integer getCategoryId() {
        return category != null ? category.getId() : null; // Trả về categoryId từ Category
    }
}
