package com.example.marketrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportsFragment extends Fragment {

    private TextView tvRevenue, tvCost, tvProfit;
    private RadioGroup radioGroupFilter;
    private List<SaleTransaction> allSalesList = new ArrayList<>();

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
        radioGroupFilter = view.findViewById(R.id.radioGroupFilter);

        // Veritabanından verileri çek
        MarketDatabase.getDatabase(getContext()).productDao().getAllSales().observe(getViewLifecycleOwner(), sales -> {
            if (sales != null) {
                allSalesList = sales;
                // Veri geldiği anda o anki seçili butona göre hesapla
                filterAndDisplay(radioGroupFilter.getCheckedRadioButtonId());
            }
        });

        radioGroupFilter.setOnCheckedChangeListener((group, checkedId) -> {
            filterAndDisplay(checkedId);
        });
    }

    private void filterAndDisplay(int checkedId) {
        List<SaleTransaction> filteredList = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        // Bugünün başlangıcı (00:00:00)
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startOfToday = cal.getTimeInMillis();

        // Hafta ve Ay hesapları
        Calendar weekCal = (Calendar) cal.clone();
        weekCal.add(Calendar.DAY_OF_YEAR, -7);
        long startOfWeek = weekCal.getTimeInMillis();

        Calendar monthCal = (Calendar) cal.clone();
        monthCal.add(Calendar.DAY_OF_YEAR, -30);
        long startOfMonth = monthCal.getTimeInMillis();

        for (SaleTransaction sale : allSalesList) {
            boolean include = false;

            // Eğer "All" seçiliyse hepsini al
            if (checkedId == R.id.radioAll) {
                include = true;
            }
            // Bugün: Satış zamanı bugünün başlangıcından büyükse
            else if (checkedId == R.id.radioToday) {
                if (sale.getTimestamp() >= startOfToday) include = true;
            }
            // Hafta
            else if (checkedId == R.id.radioWeek) {
                if (sale.getTimestamp() >= startOfWeek) include = true;
            }
            // Ay
            else if (checkedId == R.id.radioMonth) {
                if (sale.getTimestamp() >= startOfMonth) include = true;
            }

            if (include) {
                filteredList.add(sale);
            }
        }

        calculateTotals(filteredList);
    }

    private void calculateTotals(List<SaleTransaction> sales) {
        double totalRevenue = 0;
        double totalCost = 0;

        for (SaleTransaction sale : sales) {
            totalRevenue += (sale.getUnitPrice() * sale.getQuantity());
            totalCost += (sale.getUnitCost() * sale.getQuantity());
        }

        double netProfit = totalRevenue - totalCost;

        tvRevenue.setText(String.format("$%.2f", totalRevenue));
        tvCost.setText(String.format("$%.2f", totalCost));
        tvProfit.setText(String.format("$%.2f", netProfit));
    }
}