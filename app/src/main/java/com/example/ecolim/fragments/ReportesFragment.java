package com.example.ecolim.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecolim.R;
import com.example.ecolim.adapters.ResiduoAdapter;

import com.example.ecolim.models.Residuo;
import com.example.ecolim.viewmodel.ResiduoViewModel;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ReportesFragment extends Fragment {

    private ResiduoViewModel vm;
    private ResiduoAdapter adapter;
    private Button btnGenerarPDF;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reportes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recycler = view.findViewById(R.id.recyclerResiduos);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ResiduoAdapter();
        recycler.setAdapter(adapter);

        vm = new ViewModelProvider(requireActivity()).get(ResiduoViewModel.class);
        vm.residuos.observe(getViewLifecycleOwner(), adapter::submit);
        vm.cargarTodos();

        // Asignamos el botón
        btnGenerarPDF = view.findViewById(R.id.btnGenerarPDF);
        btnGenerarPDF.setOnClickListener(v -> {
            List<Residuo> listaResiduos = vm.residuos.getValue();
            if (listaResiduos != null && !listaResiduos.isEmpty()) {
                generarPDF(listaResiduos);
            } else {
                Toast.makeText(requireContext(), "No hay datos para generar el PDF", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generarPDF(List<Residuo> listaResiduos) {
        try {
            String pdfPath = requireContext().getExternalFilesDir(null).getAbsolutePath();
            File file = new File(pdfPath, "reportes_residuos.pdf");
            FileOutputStream outputStream = new FileOutputStream(file);

            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            document.add(new Paragraph("Reporte de Residuos"));
            document.add(new Paragraph("Fecha: " + new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(new java.util.Date())));
            document.add(new Paragraph(" "));

            for (Residuo residuo : listaResiduos) {
                document.add(new Paragraph("ID: " + residuo.id));
                document.add(new Paragraph("Código: " + residuo.codigo));
                document.add(new Paragraph("Tipo: " + residuo.tipo));
                document.add(new Paragraph("Peso: " + residuo.peso + " kg"));
                document.add(new Paragraph("Fecha: " + residuo.fecha));
                document.add(new Paragraph("---------------------------"));
            }

            document.close();

            Toast.makeText(requireContext(), "PDF generado en: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            compartirPDF(file);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error al generar PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void compartirPDF(File file) {
        android.net.Uri uri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(intent, "Compartir PDF"));
    }
}
