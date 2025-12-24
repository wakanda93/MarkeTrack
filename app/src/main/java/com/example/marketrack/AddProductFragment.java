package com.example.marketrack;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;

public class AddProductFragment extends Fragment {

    private TextInputEditText etName, etPurchase, etSelling, etStock;
    private Button btnSave, btnDelete, btnCancel;
    private TextView tvHeader;

    private int productId = -1; // Default to -1 (Add Mode)
    private Product currentProduct; // Holds the product being edited

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Initialize Views
        etName = view.findViewById(R.id.etProductName);
        etPurchase = view.findViewById(R.id.etPurchasePrice);
        etSelling = view.findViewById(R.id.etSellingPrice);
        etStock = view.findViewById(R.id.etStockQuantity);
        btnSave = view.findViewById(R.id.btnSaveProduct);
        btnDelete = view.findViewById(R.id.btnDeleteProduct);
        btnCancel = view.findViewById(R.id.btnCancel);
        tvHeader = view.findViewById(R.id.tvHeader);

        // 2. Check Arguments (Did we come here to Edit?)
        if (getArguments() != null) {
            productId = getArguments().getInt("productId", -1);
        }

        if (productId != -1) {
            // --- EDIT MODE ---
            tvHeader.setText("Edit Product");
            btnSave.setText("Update Product");
            btnDelete.setVisibility(View.VISIBLE); // Show Delete Button

            // Load Data from Database
            MarketDatabase.getDatabase(getContext()).productDao().getProduct(productId).observe(getViewLifecycleOwner(), product -> {
                if (product != null) {
                    currentProduct = product;
                    etName.setText(product.getName());
                    etPurchase.setText(String.valueOf(product.getPurchasePrice()));
                    etSelling.setText(String.valueOf(product.getSellingPrice()));
                    etStock.setText(String.valueOf(product.getStockQuantity()));
                }
            });
        } else {
            // --- ADD MODE ---
            tvHeader.setText("Add New Product");
            btnSave.setText("Save Product");
            btnDelete.setVisibility(View.GONE); // Hide Delete Button
        }

        // 3. Set Listeners
        btnSave.setOnClickListener(v -> saveProduct());
        btnDelete.setOnClickListener(v -> deleteProduct());
        btnCancel.setOnClickListener(v -> {
            if (getView() != null) Navigation.findNavController(getView()).popBackStack();
        });
    }

    private void saveProduct() {
        String name = etName.getText().toString().trim();
        String purchaseStr = etPurchase.getText().toString().trim();
        String sellingStr = etSelling.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(purchaseStr) || TextUtils.isEmpty(sellingStr) || TextUtils.isEmpty(stockStr)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double purchasePrice = Double.parseDouble(purchaseStr);
        double sellingPrice = Double.parseDouble(sellingStr);
        int stock = Integer.parseInt(stockStr);

        MarketDatabase.databaseWriteExecutor.execute(() -> {
            if (productId == -1) {
                // INSERT NEW
                Product newProduct = new Product(name, purchasePrice, sellingPrice, stock, 10);
                MarketDatabase.getDatabase(getContext()).productDao().insert(newProduct);
            } else {
                // UPDATE EXISTING
                if (currentProduct != null) {
                    Product updatedProduct = new Product(name, purchasePrice, sellingPrice, stock, 10);
                    updatedProduct.setId(productId); // IMPORTANT: Keep the same ID!
                    MarketDatabase.getDatabase(getContext()).productDao().update(updatedProduct);
                }
            }

            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Saved Successfully!", Toast.LENGTH_SHORT).show();
                if (getView() != null) Navigation.findNavController(getView()).popBackStack();
            });
        });
    }

    private void deleteProduct() {
        if (currentProduct == null) return;

        MarketDatabase.databaseWriteExecutor.execute(() -> {
            MarketDatabase.getDatabase(getContext()).productDao().delete(currentProduct);

            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Product Deleted!", Toast.LENGTH_SHORT).show();
                if (getView() != null) Navigation.findNavController(getView()).popBackStack();
            });
        });
    }
}