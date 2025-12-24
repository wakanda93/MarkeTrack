package com.example.marketrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InventoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Setup RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductAdapter();
        recyclerView.setAdapter(adapter);

        // --- NEW: Handle Product Clicks (Edit Mode) ---
        adapter.setOnProductClickListener(product -> {
            Bundle args = new Bundle();
            args.putInt("productId", product.getId()); // Pass the ID
            Navigation.findNavController(view).navigate(R.id.action_inventory_to_addProduct, args);
        });

        // 2. Setup FAB (Add New Mode)
        FloatingActionButton fab = view.findViewById(R.id.fabAddProduct);
        fab.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("productId", -1); // -1 means New Product
            Navigation.findNavController(view).navigate(R.id.action_inventory_to_addProduct, args);
        });

        // 3. Observe Database
        MarketDatabase.getDatabase(getContext()).productDao().getAllProducts().observe(getViewLifecycleOwner(), products -> {
            adapter.setProducts(products);
        });
    }
}