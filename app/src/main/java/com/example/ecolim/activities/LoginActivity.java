package com.example.ecolim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecolim.R;
import com.example.ecolim.db.AppDatabase;
import com.example.ecolim.models.Usuario;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Inicializar base de datos
        AppDatabase db = AppDatabase.getInstance(this);

        // Insertar usuarios de prueba si no existen
        if (db.usuarioDao().getUsuariosCount() == 0) {
            db.usuarioDao().insert(new Usuario("David", "david@gmail.com", "1234"));
            db.usuarioDao().insert(new Usuario("Juan Pérez", "juan@ecolim.com", "abcd"));
        }

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa email y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validación en base de datos
            Usuario usuario = db.usuarioDao().login(email, pass);
            if (usuario != null) {
                // Guardamos en SharedPreferences
                getSharedPreferences("ECOLIM_PREFS", MODE_PRIVATE)
                        .edit()
                        .putString("usuario", usuario.nombre)
                        .apply();

                // Ir al MainActivity
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
