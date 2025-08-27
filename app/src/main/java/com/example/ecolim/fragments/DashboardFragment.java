package com.example.ecolim.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecolim.R;
import com.example.ecolim.models.Residuo;
import com.example.ecolim.viewmodel.ResiduoViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private ResiduoViewModel vm;
    private TextView tvTotalReg, tvTotalKg, tvUltimo, tvMasValioso, tvPorcentajeReciclados, tvUbicacionFrecuente;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        tvTotalReg = v.findViewById(R.id.tvTotalReg);
        tvTotalKg = v.findViewById(R.id.tvTotalKg);
        tvUltimo = v.findViewById(R.id.tvUltimo);
        tvMasValioso = v.findViewById(R.id.tvMasValioso);
        tvPorcentajeReciclados = v.findViewById(R.id.tvPorcentajeReciclados);
        tvUbicacionFrecuente = v.findViewById(R.id.tvUbicacionFrecuente);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(requireActivity()).get(ResiduoViewModel.class);

        vm.residuos.observe(getViewLifecycleOwner(), list -> {
            int total = list.size();
            double kg = 0;
            String ultimo = "-";

            Residuo masValioso = null;
            int reciclados = 0;
            Map<String, Integer> ubicaciones = new HashMap<>();

            if (!list.isEmpty()) {
                ultimo = list.get(0).fecha; // asumiendo orden por fecha desc
            }

            for (Residuo r : list) {
                kg += r.peso;

                // Más valioso
                if (masValioso == null || r.valorAproximado > masValioso.valorAproximado) {
                    masValioso = r;
                }

                // Contar reciclados (si tipo contiene "reciclado")
                if (r.tipo != null && r.tipo.toLowerCase(Locale.ROOT).contains("reciclado")) {
                    reciclados++;
                }

                // Contar ubicación más frecuente
                if (r.origen != null && !r.origen.isEmpty()) {
                    ubicaciones.put(r.origen, ubicaciones.getOrDefault(r.origen, 0) + 1);
                }
            }

            // Calcular ubicación más frecuente
            String ubicacionFrecuente = "-";
            int maxCount = 0;
            for (Map.Entry<String, Integer> entry : ubicaciones.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    ubicacionFrecuente = entry.getKey();
                }
            }

            // Porcentaje reciclados
            double porcentaje = total > 0 ? (reciclados * 100.0 / total) : 0;

            // Setear datos en la UI
            tvTotalReg.setText(String.valueOf(total));
            tvTotalKg.setText(String.format(Locale.getDefault(), "%.2f kg", kg));
            tvUltimo.setText(ultimo);
            tvMasValioso.setText(masValioso != null ? masValioso.tipo + " ($" + masValioso.valorAproximado + ")" : "-");
            tvPorcentajeReciclados.setText(String.format(Locale.getDefault(), "%.1f%%", porcentaje));
            tvUbicacionFrecuente.setText(ubicacionFrecuente);
        });

        vm.cargarTodos();
    }
}
