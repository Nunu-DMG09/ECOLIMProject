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
import com.example.ecolim.fragments.UsuarioFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String usuario = getSharedPreferences("ECOLIM_PREFS", MODE_PRIVATE)
                .getString("usuario", "Usuario");

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setTitle("Bienvenido " + usuario);

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_logout) {

                getSharedPreferences("ECOLIM_PREFS", MODE_PRIVATE).edit().clear().apply();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            }
            return false;
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            if (item.getItemId() == R.id.nav_dashboard) {
                selected = new DashboardFragment();
            } else if (item.getItemId() == R.id.nav_registro) {
                selected = new RegistroFragment();
            } else if (item.getItemId() == R.id.nav_reportes) {
                selected = new ReportesFragment();
            } else if (item.getItemId() == R.id.nav_usuario) {
                selected = new UsuarioFragment();
            }
            if (selected != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selected)
                        .commit();
            }
            return true;
        });

        bottomNav.setSelectedItemId(R.id.nav_dashboard);
    }
}
