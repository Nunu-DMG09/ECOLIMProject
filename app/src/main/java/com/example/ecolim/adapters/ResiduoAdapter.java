package com.example.ecolim.adapters;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecolim.R;
import com.example.ecolim.models.Residuo;
import java.util.ArrayList;
import java.util.List;

public class ResiduoAdapter extends RecyclerView.Adapter<ResiduoAdapter.VH> {
    private final List<Residuo> data = new ArrayList<>();

    public void submit(List<Residuo> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_residuo, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Residuo r = data.get(pos);
        h.tvTipo.setText(r.tipo);
        h.tvPeso.setText(String.format("%.2f kg", r.peso));
        h.tvFecha.setText(r.fecha);
        h.tvCodigo.setText(r.codigo == null ? "-" : r.codigo);
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTipo, tvPeso, tvFecha, tvCodigo;
        VH(@NonNull View v) {
            super(v);
            tvTipo = v.findViewById(R.id.tvTipo);
            tvPeso = v.findViewById(R.id.tvPeso);
            tvFecha= v.findViewById(R.id.tvFecha);
            tvCodigo= v.findViewById(R.id.tvCodigo);
        }
    }
}
