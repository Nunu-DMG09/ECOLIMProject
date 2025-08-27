package com.example.ecolim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.ecolim.R;
import com.example.ecolim.fragments.DashboardFragment;
import com.example.ecolim.fragments.RegistroFragment;
import com.example.ecolim.fragments.ReportesFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener nombre usuario
        String usuario = getSharedPreferences("ECOLIM_PREFS", MODE_PRIVATE)
                .getString("usuario", "Usuario");

        // Configurar Toolbar
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setTitle("Bienvenido " + usuario);

        // Acción cerrar sesión
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_logout) {
                // Borrar sesión
                getSharedPreferences("ECOLIM_PREFS", MODE_PRIVATE).edit().clear().apply();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            }
            return false;
        });

        // Navegación inferior
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            if (item.getItemId() == R.id.nav_dashboard) {
                selected = new DashboardFragment();
            } else if (item.getItemId() == R.id.nav_registro) {
                selected = new RegistroFragment();
            } else if (item.getItemId() == R.id.nav_reportes) {
                selected = new ReportesFragment();
            }
            if (selected != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selected)
                        .commit();
            }
            return true;
        });

        // Por defecto Dashboard
        bottomNav.setSelectedItemId(R.id.nav_dashboard);
    }
}
