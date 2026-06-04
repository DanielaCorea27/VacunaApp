package com.example.pinchaapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.pinchaapp.dto.CentroDto;
import com.example.pinchaapp.dto.RespuestaDto;
import com.example.pinchaapp.network.ApiClient;
import com.example.pinchaapp.network.ApiService;
import com.example.pinchaapp.util.UbicacionHelper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CentrosDeVacunacion extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MaterialToolbar toolbar;
    private TextView txtEstadoCentros;
    private CardView cardEstado;

    private int idPerfil;
    private static final double RADIO_KM = 50.0; // Extendemos un poco el rango para El Salvador

    // Diccionario para asociar cada Marcador del mapa con su objeto CentroDto
    private Map<Marker, CentroDto> markerCentroMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_centros_de_vacunacion);

        // Inicializar componentes visuales
        toolbar = findViewById(R.id.toolbar);
        txtEstadoCentros = findViewById(R.id.txtEstadoCentros);
        cardEstado = findViewById(R.id.cardEstado);

        // Configurar la navegación de regreso limpia
        toolbar.setNavigationOnClickListener(v -> finish());

        // Recuperar el ID del Miembro que viene del Dashboard
        idPerfil = getIntent().getIntExtra("idPerfil", 0);

        // Inicializar de manera asíncrona el Fragmento de Google Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Estilo de mapa limpio y configuraciones iniciales
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        // Listener: Al hacer clic sobre el globo informativo del marcador
        mMap.setOnInfoWindowClickListener(marker -> {
            CentroDto centroSeleccionado = markerCentroMap.get(marker);
            if (centroSeleccionado != null) {
                // Navegar a la pantalla de agendar vacuna pasando el contexto geográfico
                Intent intent = new Intent(this, AgendarVacuna.class);
                intent.putExtra("idPerfil", idPerfil);
                intent.putExtra("idCentro", centroSeleccionado.getId());
                intent.putExtra("nombreCentro", centroSeleccionado.getNombre());
                startActivity(intent);
            }
        });

        // Disparar flujo de GPS y consumo de API
        comprobarPermisosYUbicacion();
    }

    private void comprobarPermisosYUbicacion() {
        if (UbicacionHelper.tienePermiso(this)) {
            obtenerCoordenadasDispositivo();
        } else {
            UbicacionHelper.pedirPermiso(this);
        }
    }

    private void obtenerCoordenadasDispositivo() {
        cardEstado.setVisibility(View.VISIBLE);
        txtEstadoCentros.setText("Obteniendo coordenadas GPS...");

        try {
            // Activar la capa nativa del punto azul del usuario en el mapa
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException ignored) {}

        UbicacionHelper.obtenerUbicacion(this, new UbicacionHelper.UbicacionCallback() {
            @Override
            public void onUbicacionObtenida(double latitud, double longitud) {
                // Centrar la cámara nativa en la posición real del usuario
                LatLng miUbicacion = new LatLng(latitud, longitud);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion, 13f));

                // Consumir el backend C#
                buscarCentrosEnServidor(latitud, longitud);
            }

            @Override
            public void onError(String mensaje) {
                txtEstadoCentros.setText("Error GPS: " + mensaje + ". Cargando mapa base.");
                cardEstado.postDelayed(() -> cardEstado.setVisibility(View.GONE), 3000);
            }
        });
    }

    private void buscarCentrosEnServidor(double lat, double lng) {
        txtEstadoCentros.setText("Buscando centros de vacunación cercanos...");
        ApiService api = ApiClient.getInstance().create(ApiService.class);

        // Ejecución del endpoint espacial
        api.getCentrosCercanos(lat, lng, RADIO_KM).enqueue(new Callback<RespuestaDto<List<CentroDto>>>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaDto<List<CentroDto>>> call,
                                   @NonNull Response<RespuestaDto<List<CentroDto>>> response) {
                cardEstado.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().isExito()) {
                    pintarCentrosEnMapa(response.body().getData());
                } else {
                    Toast.makeText(CentrosDeVacunacion.this, "Sin centros en el rango de cobertura.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaDto<List<CentroDto>>> call, @NonNull Throwable t) {
                txtEstadoCentros.setText("Falla de red: " + t.getMessage());
            }
        });
    }

    private void pintarCentrosEnMapa(List<CentroDto> centros) {
        if (centros == null || centros.isEmpty()) return;

        mMap.clear(); // Limpiar marcadores viejos
        markerCentroMap.clear();

        for (CentroDto centro : centros) {
            if (centro.getLatitud() == null || centro.getLongitud() == null) continue;

            LatLng posicion = new LatLng(centro.getLatitud().doubleValue(), centro.getLongitud().doubleValue());

            // Personalización visual del marcador
            MarkerOptions opcionesMarcador = new MarkerOptions()
                    .position(posicion)
                    .title(centro.getNombre())
                    .snippet("Horario: " + centro.getHorario() + " | Toca aquí para ver vacunas")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            Marker marker = mMap.addMarker(opcionesMarcador);

            // Guardamos la relación en memoria para recuperarla en el clic
            markerCentroMap.put(marker, centro);
        }
    }

    // Gestionar respuesta del cuadro de diálogo nativo de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == UbicacionHelper.REQUEST_PERMISO_UBICACION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerCoordenadasDispositivo();
            } else {
                txtEstadoCentros.setText("Permiso denegado. No se pueden mapear centros cercanos.");
            }
        }
    }
}