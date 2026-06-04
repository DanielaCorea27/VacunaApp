package com.example.pinchaapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.adapters.ProximaDosisAdapter;
import com.example.pinchaapp.dto.HistorialDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProximasDosisActivity extends AppCompatActivity {

    private RecyclerView rvProximasDosis;
    private TextView txtEstado;
    private ApiService api;
    private int idPerfil;
    private String nombrePerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximas_dosis);

        idPerfil     = getIntent().getIntExtra("idPerfil", 0);
        nombrePerfil = getIntent().getStringExtra("nombre");

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        if (nombrePerfil != null)
            toolbar.setSubtitle(nombrePerfil);

        rvProximasDosis = findViewById(R.id.rvProximasDosis);
        txtEstado       = findViewById(R.id.txtEstado);

        rvProximasDosis.setLayoutManager(new LinearLayoutManager(this));

        api = ApiClient.getInstance().create(ApiService.class);
        cargarProximasDosis();
    }

    private void cargarProximasDosis() {
        txtEstado.setVisibility(View.VISIBLE);
        txtEstado.setText("Cargando…");

        api.getHistorial(idPerfil).enqueue(
                new Callback<RespuestaDto<List<HistorialDto.HistorialResponseDto>>>() {

            @Override
            public void onResponse(
                    Call<RespuestaDto<List<HistorialDto.HistorialResponseDto>>> call,
                    Response<RespuestaDto<List<HistorialDto.HistorialResponseDto>>> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isExito()) {

                    List<HistorialDto.ProximaDosisResponseDto> proximasDosis =
                            filtrarYMapear(response.body().getData());

                    if (proximasDosis.isEmpty()) {
                        txtEstado.setText("No hay dosis programadas próximamente.");
                        return;
                    }

                    txtEstado.setVisibility(View.GONE);
                    rvProximasDosis.setAdapter(new ProximaDosisAdapter(proximasDosis));

                } else {
                    txtEstado.setText("No se pudo cargar el historial.");
                }
            }

            @Override
            public void onFailure(
                    Call<RespuestaDto<List<HistorialDto.HistorialResponseDto>>> call,
                    Throwable t) {
                txtEstado.setText("Error de conexión: " + t.getMessage());
            }
        });
    }

    // Filtra solo los registros con próxima dosis en el futuro y los convierte
    private List<HistorialDto.ProximaDosisResponseDto> filtrarYMapear(
            List<HistorialDto.HistorialResponseDto> historial) {

        List<HistorialDto.ProximaDosisResponseDto> resultado = new ArrayList<>();
        if (historial == null) return resultado;

        Date hoy = new Date();

        for (HistorialDto.HistorialResponseDto h : historial) {
            String proximaStr = h.getProximaDosis();
            if (proximaStr == null || proximaStr.trim().isEmpty()) continue;

            Date fechaProxima = parsearFecha(proximaStr);
            if (fechaProxima == null || fechaProxima.before(hoy)) continue;

            long diffMs   = fechaProxima.getTime() - hoy.getTime();
            int  diasRest = (int) (diffMs / (1000L * 60 * 60 * 24));

            HistorialDto.ProximaDosisResponseDto dto = new HistorialDto.ProximaDosisResponseDto();
            dto.setMiembro(nombrePerfil != null ? nombrePerfil : "");
            dto.setVacuna(h.getVacuna());
            dto.setProximaDosis(proximaStr);
            dto.setDiasRestantes(diasRest);

            resultado.add(dto);
        }

        // Ordenar por días restantes (más próxima primero)
        resultado.sort((a, b) -> {
            int da = a.getDiasRestantes() != null ? a.getDiasRestantes() : 999;
            int db = b.getDiasRestantes() != null ? b.getDiasRestantes() : 999;
            return Integer.compare(da, db);
        });

        return resultado;
    }

    private Date parsearFecha(String fechaStr) {
        String[] formatos = {
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd"
        };
        String limpia = fechaStr.split("\\.")[0].replace("Z", "");
        for (String formato : formatos) {
            try {
                return new SimpleDateFormat(formato, Locale.getDefault()).parse(
                        formato.contains("T") ? limpia : fechaStr);
            } catch (Exception ignored) {}
        }
        return null;
    }
}
