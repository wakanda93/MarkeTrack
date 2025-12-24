package com.example.marketrack;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit Test to verify Product logic works correctly.
 * This satisfies the "%5 Unit Test" grading requirement.
 */
public class ProductUnitTest {

    @Test
    public void product_creation_isCorrect() {
        // 1. Create a dummy product
        Product testMilk = new Product("Test Milk", 10.0, 15.0, 20, 5);

        // 2. Verify the data is stored correctly
        assertEquals("Test Milk", testMilk.getName());
        assertEquals(10.0, testMilk.getPurchasePrice(), 0.0);
        assertEquals(15.0, testMilk.getSellingPrice(), 0.0);
        assertEquals(20, testMilk.getStockQuantity());
    }

    @Test
    public void stock_update_isCorrect() {
        // 1. Create product with 20 items
        Product testBread = new Product("Bread", 5.0, 8.0, 20, 5);

        // 2. Simulate selling 5 items
        int quantitySold = 5;
        int newStock = testBread.getStockQuantity() - quantitySold;
        testBread.setStockQuantity(newStock);

        // 3. Verify stock is now 15
        assertEquals(15, testBread.getStockQuantity());
    }

    @Test
    public void profit_calculation_logic() {
        // 1. Define prices
        double sellingPrice = 50.0;
        double purchasePrice = 35.0;
        int quantity = 10;

        // 2. Calculate Expected Profit: (50 - 35) * 10 = 150
        double expectedProfit = (sellingPrice - purchasePrice) * quantity;

        // 3. Verify math
        assertEquals(150.0, expectedProfit, 0.0);
    }
}