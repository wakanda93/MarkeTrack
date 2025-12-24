package com.example.marketrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InventoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private TextView tvEmptyView;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        tvEmptyView = view.findViewById(R.id.tvEmptyView);
        searchView = view.findViewById(R.id.searchView);
        FloatingActionButton fab = view.findViewById(R.id.fabAddProduct);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductAdapter();
        recyclerView.setAdapter(adapter);

        // --- ARAMA DİNLEYİCİSİ ---
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Her harf girişinde listeyi filtrele
                adapter.filterList(newText);
                return true;
            }
        });

        // Ürün Tıklama (Düzenleme Modu)
        adapter.setOnProductClickListener(product -> {
            Bundle args = new Bundle();
            args.putInt("productId", product.getId());
            Navigation.findNavController(view).navigate(R.id.action_inventory_to_addProduct, args);
        });

        // Yeni Ürün Ekleme
        fab.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("productId", -1);
            Navigation.findNavController(view).navigate(R.id.action_inventory_to_addProduct, args);
        });

        // Veritabanı Gözlemcisi
        MarketDatabase.getDatabase(getContext()).productDao().getAllProducts().observe(getViewLifecycleOwner(), products -> {
            adapter.setProducts(products);

            // Eğer liste boşsa veya arama sonucu boşsa uyarı göster
            if (products.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                tvEmptyView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                tvEmptyView.setVisibility(View.GONE);
            }
        });
    }
}