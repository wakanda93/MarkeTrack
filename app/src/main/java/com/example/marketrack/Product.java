package com.example.marketrack;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Authors: Tayfun Özgür, Emre Kırdım
@Entity(tableName = "product_table")
public class Product {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private double purchasePrice;
    private double sellingPrice;
    private int stockQuantity;
    private int lowStockThreshold;

    // Constructor
    public Product(String name, double purchasePrice, double sellingPrice, int stockQuantity, int lowStockThreshold) {
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.stockQuantity = stockQuantity;
        this.lowStockThreshold = lowStockThreshold;
    }

    // Getters and Setters (Required for Room)
    public void setId(int id) { this.id = id; }
    public int getId() { return id; }

    public String getName() { return name; }
    public double getPurchasePrice() { return purchasePrice; }
    public double getSellingPrice() { return sellingPrice; }
    public int getStockQuantity() { return stockQuantity; }
    public int getLowStockThreshold() { return lowStockThreshold; }

    // Setter for updating stock
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}