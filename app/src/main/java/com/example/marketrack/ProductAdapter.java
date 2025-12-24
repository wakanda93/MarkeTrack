package com.example.marketrack;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList = new ArrayList<>();
    private OnProductClickListener listener; // Click Listener

    // Interface to handle clicks
    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public void setOnProductClickListener(OnProductClickListener listener) {
        this.listener = listener;
    }

    public void setProducts(List<Product> products) {
        this.productList = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product currentProduct = productList.get(position);

        holder.tvName.setText(currentProduct.getName());
        holder.tvPrice.setText("Sell: " + currentProduct.getSellingPrice());
        holder.tvStock.setText("Stock: " + currentProduct.getStockQuantity());

        // --- UC8: Low Stock Alert (Visual) ---
        // If stock is low, change the card background color or text color
        if (currentProduct.getStockQuantity() <= currentProduct.getLowStockThreshold()) {
            holder.tvStock.setTextColor(Color.RED);
            holder.tvStock.setText("Stock: " + currentProduct.getStockQuantity() + " (LOW!)");
        } else {
            holder.tvStock.setTextColor(Color.BLACK);
        }

        // Handle Item Click (Open Edit Screen)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(currentProduct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvPrice, tvStock;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvStock = itemView.findViewById(R.id.tvProductStock);
        }
    }
}