package com.example.marketrack;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    // Returns a LiveData list so the UI updates automatically when data changes
    @Query("SELECT * FROM product_table ORDER BY name ASC")
    LiveData<List<Product>> getAllProducts();

    // For the "Low Stock Alert" feature
    @Query("SELECT * FROM product_table WHERE stockQuantity <= lowStockThreshold")
    LiveData<List<Product>> getLowStockProducts();
    // UC4: Save a record of the sale
    @Insert
    void insertSale(SaleTransaction sale);

    // UC7: Get all sales (We will use this later for the Reports screen)
    @Query("SELECT * FROM sales_table ORDER BY timestamp DESC")
    LiveData<List<SaleTransaction>> getAllSales();

    // UC2: Get a single product to edit it
    @Query("SELECT * FROM product_table WHERE id = :id")
    LiveData<Product> getProduct(int id);
}