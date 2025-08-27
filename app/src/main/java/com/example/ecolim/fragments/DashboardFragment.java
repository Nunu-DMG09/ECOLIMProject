package com.example.ecolim.fragments;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.ecolim.R;
import com.example.ecolim.viewmodel.ResiduoViewModel;

public class DashboardFragment extends Fragment {
    private ResiduoViewModel vm;
    private TextView tvTotalReg, tvTotalKg, tvUltimo;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        tvTotalReg = v.findViewById(R.id.tvTotalReg);
        tvTotalKg  = v.findViewById(R.id.tvTotalKg);
        tvUltimo   = v.findViewById(R.id.tvUltimo);
        return v;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(requireActivity()).get(ResiduoViewModel.class);
        vm.residuos.observe(getViewLifecycleOwner(), list -> {
            int total = list.size();
            double kg = 0;
            String ultimo = "-";
            if (!list.isEmpty()) {
                ultimo = list.get(0).fecha; // asumiendo orden por fecha desc al cargar
            }
            for (var r : list) kg += r.peso;
            tvTotalReg.setText(String.valueOf(total));
            tvTotalKg.setText(String.format("%.2f kg", kg));
            tvUltimo.setText(ultimo);
        });
        vm.cargarTodos(); // carga desde SQLite
    }
}
