package com.example.marketrack;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class RecordSaleFragment extends Fragment {

    private Spinner spinnerProducts;
    private TextInputEditText etQuantity;
    private Button btnRecord;

    // We keep the list of actual Product objects here so we can get IDs and Prices later
    private List<Product> loadedProducts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record_sale, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerProducts = view.findViewById(R.id.spinnerProducts);
        etQuantity = view.findViewById(R.id.etQuantity);
        btnRecord = view.findViewById(R.id.btnRecordSale);

        // 1. Load Products into the Spinner (Dropdown)
        loadProductsToSpinner();

        // 2. Handle the "Record Sale" Button Click
        btnRecord.setOnClickListener(v -> performSale());
    }

    private void loadProductsToSpinner() {
        MarketDatabase.getDatabase(getContext()).productDao().getAllProducts().observe(getViewLifecycleOwner(), products -> {
            loadedProducts = products;

            // Create a list of just names for the spinner display
            List<String> productNames = new ArrayList<>();
            for (Product p : products) {
                // Show Name and Current Stock in the dropdown (e.g., "Milk (10)")
                productNames.add(p.getName() + " (" + p.getStockQuantity() + " left)");
            }

            // Attach data to spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, productNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerProducts.setAdapter(adapter);
        });
    }

    private void performSale() {
        // Validation: Is a product selected?
        if (loadedProducts.isEmpty() || spinnerProducts.getSelectedItemPosition() == -1) {
            Toast.makeText(getContext(), "No products available!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validation: Is quantity entered?
        String qtyStr = etQuantity.getText().toString().trim();
        if (TextUtils.isEmpty(qtyStr)) {
            etQuantity.setError("Enter quantity");
            return;
        }

        int quantitySold = Integer.parseInt(qtyStr);
        if (quantitySold <= 0) {
            etQuantity.setError("Quantity must be > 0");
            return;
        }

        // Get the selected product object
        int selectedIndex = spinnerProducts.getSelectedItemPosition();
        Product selectedProduct = loadedProducts.get(selectedIndex);

        // Validation: Do we have enough stock?
        if (selectedProduct.getStockQuantity() < quantitySold) {
            Toast.makeText(getContext(), "Not enough stock! Only " + selectedProduct.getStockQuantity() + " left.", Toast.LENGTH_LONG).show();
            return;
        }

        // --- THE TRANSACTION LOGIC ---
        MarketDatabase.databaseWriteExecutor.execute(() -> {

            // 1. Decrease Stock
            int newStock = selectedProduct.getStockQuantity() - quantitySold;
            selectedProduct.setStockQuantity(newStock);
            MarketDatabase.getDatabase(getContext()).productDao().update(selectedProduct);

            // 2. Save the Sale Record (For Reports)
            SaleTransaction sale = new SaleTransaction(
                    selectedProduct.getId(),
                    selectedProduct.getName(),
                    quantitySold,
                    selectedProduct.getSellingPrice(),
                    selectedProduct.getPurchasePrice(),
                    System.currentTimeMillis() // Current Time
            );
            MarketDatabase.getDatabase(getContext()).productDao().insertSale(sale);

            // 3. Update UI
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Sale Recorded Successfully!", Toast.LENGTH_SHORT).show();
                etQuantity.setText(""); // Clear input
            });
        });
    }
}