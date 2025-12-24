package com.example.marketrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.List;

public class ReportsFragment extends Fragment {

    private TextView tvRevenue, tvCost, tvProfit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reports, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvRevenue = view.findViewById(R.id.tvTotalRevenue);
        tvCost = view.findViewById(R.id.tvTotalCost);
        tvProfit = view.findViewById(R.id.tvNetProfit);

        // Calculate Totals from Database
        MarketDatabase.getDatabase(getContext()).productDao().getAllSales().observe(getViewLifecycleOwner(), sales -> {
            calculateAndDisplayTotals(sales);
        });
    }

    private void calculateAndDisplayTotals(List<SaleTransaction> sales) {
        double totalRevenue = 0;
        double totalCost = 0;

        if (sales != null) {
            for (SaleTransaction sale : sales) {
                // Revenue = Price * Quantity
                totalRevenue += (sale.getUnitPrice() * sale.getQuantity());

                // Cost = Cost * Quantity
                totalCost += (sale.getUnitCost() * sale.getQuantity());
            }
        }

        double netProfit = totalRevenue - totalCost;

        // Display results formatted as currency
        tvRevenue.setText(String.format("$%.2f", totalRevenue));
        tvCost.setText(String.format("$%.2f", totalCost));
        tvProfit.setText(String.format("$%.2f", netProfit));
    }
}