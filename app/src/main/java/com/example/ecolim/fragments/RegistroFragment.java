package com.example.ecolim.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecolim.R;
import com.example.ecolim.db.AppDatabase;
import com.example.ecolim.models.Residuo;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistroFragment extends Fragment {

    private EditText etTipo, etPeso;
    private Button btnGuardar, btnEscanear;
    private TextView tvCodigo;
    private String codigoQR = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        etTipo = view.findViewById(R.id.etTipo);
        etPeso = view.findViewById(R.id.etPeso);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnEscanear = view.findViewById(R.id.btnEscanear);
        tvCodigo = view.findViewById(R.id.tvCodigo);

        btnGuardar.setOnClickListener(v -> guardarResiduo());
        btnEscanear.setOnClickListener(v -> iniciarEscaneo());

        return view;
    }

    private void iniciarEscaneo() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Escanee el código del residuo");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            codigoQR = result.getContents();
            tvCodigo.setText("Código: " + codigoQR);
        } else {
            Toast.makeText(getContext(), "No se detectó código", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarResiduo() {
        String tipo = etTipo.getText().toString().trim();
        String pesoStr = etPeso.getText().toString().trim();

        if (tipo.isEmpty() || pesoStr.isEmpty()) {
            Toast.makeText(getContext(), "Complete los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double peso;
        try {
            peso = Double.parseDouble(pesoStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Ingrese un peso válido", Toast.LENGTH_SHORT).show();
            return;
        }

        String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Residuo residuo = new Residuo(tipo, peso, fecha, codigoQR.isEmpty() ? "-" : codigoQR);

        btnGuardar.setEnabled(false);

        new Thread(() -> {
            AppDatabase.getInstance(getContext()).residuoDao().insert(residuo);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Registro guardado", Toast.LENGTH_SHORT).show();
                    etTipo.setText("");
                    etPeso.setText("");
                    tvCodigo.setText("Código: -");
                    codigoQR = "";
                    btnGuardar.setEnabled(true);
                });
            }
        }).start();
    }
}
