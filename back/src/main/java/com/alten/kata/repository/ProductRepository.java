package com.alten.kata.repository;

import com.alten.kata.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {
}