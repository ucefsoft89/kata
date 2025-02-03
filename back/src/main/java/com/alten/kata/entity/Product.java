package com.alten.kata.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name = "products")
@Data
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String code;
    private String name;
    private String description;
    private String image;
    private String category;
    private double price;
    private int quantity;
    private String internalReference;
    private int shellId;
    private  InventoryStatus inventoryStatus;
    private double rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

   }

