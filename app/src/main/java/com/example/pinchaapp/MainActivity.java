package com.example.pinchaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pinchaapp.database.VacunAppDatabase;
import com.example.pinchaapp.database.dao.UsuarioDao;
import com.example.pinchaapp.database.entities.Usuario;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnCrearCuenta;
    private TextView tvOlvido;
    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // DAO
        usuarioDao = VacunAppDatabase.getInstance(this).usuarioDao();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        tvOlvido = findViewById(R.id.tvOlvido);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                Usuario usuario = usuarioDao.login(email, password);

                runOnUiThread(() -> {
                    if (usuario != null) {
                        Toast.makeText(this, "Bienvenido " + usuario.getNombre() + "!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, pantalla_dashboard.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        btnCrearCuenta.setOnClickListener(v ->
                startActivity(new Intent(this, CrearCuenta.class))
        );

        tvOlvido.setOnClickListener(v ->
                Toast.makeText(this, "aun me falta", Toast.LENGTH_SHORT).show()
        );

    }
}