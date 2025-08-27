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
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class DashboardFragment extends Fragment {

    private ResiduoViewModel vm;
    private TextView tvTotalReg, tvTotalKg, tvUltimo, tvMasValioso, tvPorcentajeReciclados, tvUbicacionFrecuente;
    private TextView tvCategorias, tvMasPesado, tvValorTotal, tvUltimaUbicacion; // NUEVOS

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // IDs existentes
        tvTotalReg = v.findViewById(R.id.tvTotalReg);
        tvTotalKg = v.findViewById(R.id.tvTotalKg);
        tvUltimo = v.findViewById(R.id.tvUltimo);
        tvMasValioso = v.findViewById(R.id.tvMasValioso);
        tvPorcentajeReciclados = v.findViewById(R.id.tvPorcentajeReciclados);
        tvUbicacionFrecuente = v.findViewById(R.id.tvUbicacionFrecuente);

        // NUEVOS TextViews (debes agregarlos en tu XML)
        tvCategorias = v.findViewById(R.id.tvCategorias);
        tvMasPesado = v.findViewById(R.id.tvMasPesado);
        tvValorTotal = v.findViewById(R.id.tvValorTotal);
        tvUltimaUbicacion = v.findViewById(R.id.tvUltimaUbicacion);

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
            Residuo masPesado = null;
            int reciclados = 0;
            double valorTotal = 0;

            Map<String, Integer> ubicaciones = new HashMap<>();
            Set<String> categoriasUnicas = new HashSet<>();

            if (!list.isEmpty()) {
                ultimo = list.get(0).fecha; // asumiendo orden por fecha desc
            }

            for (Residuo r : list) {
                kg += r.peso;
                valorTotal += r.valorAproximado;

                // Más valioso
                if (masValioso == null || r.valorAproximado > masValioso.valorAproximado) {
                    masValioso = r;
                }

                // Más pesado
                if (masPesado == null || r.peso > masPesado.peso) {
                    masPesado = r;
                }

                // Contar reciclados
                if (r.tipo != null && r.tipo.toLowerCase(Locale.ROOT).contains("reciclable")) {
                    reciclados++;
                }

                // Categorías únicas
                if (r.categoria != null && !r.categoria.isEmpty()) {
                    categoriasUnicas.add(r.categoria);
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

            // Última ubicación registrada
            String ultimaUbicacion = list.isEmpty() || list.get(0).ubicacion == null ? "-" : list.get(0).ubicacion;

            // Setear datos en la UI
            tvTotalReg.setText(String.valueOf(total));
            tvTotalKg.setText(String.format(Locale.getDefault(), "%.2f kg", kg));
            tvUltimo.setText(ultimo);
            tvMasValioso.setText(masValioso != null ? masValioso.tipo + " ($" + masValioso.valorAproximado + ")" : "-");
            if (total > 0) {
                tvPorcentajeReciclados.setText(String.format(Locale.getDefault(), "%.1f%%", porcentaje));
            } else {
                tvPorcentajeReciclados.setText("Sin datos");
            }
            tvUbicacionFrecuente.setText(ubicacionFrecuente);

            // NUEVOS campos
            tvCategorias.setText(total > 0 ? categoriasUnicas.size() + " categorías" : "Sin datos");
            tvMasPesado.setText(masPesado != null ? masPesado.nombre + " (" + masPesado.peso + " kg)" : "-");
            tvValorTotal.setText("$" + valorTotal);
            tvUltimaUbicacion.setText(ultimaUbicacion);
        });

        vm.cargarTodos();
    }
}
