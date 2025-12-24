package com.example.marketrack;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sales_table")
public class SaleTransaction {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int productId;       // Which product was sold?
    private String productName;  // Saved in case the product is deleted later
    private int quantity;        // How many?
    private double unitPrice;    // Selling Price at the time of sale
    private double unitCost;     // Purchase Price at the time of sale
    private long timestamp;      // When did it happen? (For Daily/Monthly reports)

    // Constructor
    public SaleTransaction(int productId, String productName, int quantity, double unitPrice, double unitCost, long timestamp) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unitCost = unitCost;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public void setId(int id) { this.id = id; }
    public int getId() { return id; }

    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getUnitCost() { return unitCost; }
    public long getTimestamp() { return timestamp; }
}