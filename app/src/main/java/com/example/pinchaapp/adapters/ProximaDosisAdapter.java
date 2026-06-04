package com.example.pinchaapp.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinchaapp.R;
import com.example.pinchaapp.dto.HistorialDto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProximaDosisAdapter extends RecyclerView.Adapter<ProximaDosisAdapter.VH> {

    private final List<HistorialDto.ProximaDosisResponseDto> lista;

    public ProximaDosisAdapter(List<HistorialDto.ProximaDosisResponseDto> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_proxima_dosis, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        HistorialDto.ProximaDosisResponseDto item = lista.get(position);

        h.txtVacuna.setText(item.getVacuna() != null ? item.getVacuna() : "Vacuna");
        h.txtMiembro.setText(item.getMiembro() != null ? item.getMiembro() : "");
        h.txtFecha.setText(formatearFechaProxima(item.getProximaDosis()));

        int dias = calcularDias(item);
        aplicarIndicador(h, dias);
    }

    @Override
    public int getItemCount() { return lista.size(); }

    private int calcularDias(HistorialDto.ProximaDosisResponseDto item) {
        if (item.getDiasRestantes() != null) return item.getDiasRestantes();

        // Calcular desde la fecha si la API no trae diasRestantes
        String fechaStr = item.getProximaDosis();
        if (fechaStr == null || fechaStr.isEmpty()) return 999;
        try {
            String limpia = fechaStr.split("\\.")[0].replace("Z", "");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date fecha = sdf.parse(limpia);
            if (fecha == null) return 999;
            long diff = fecha.getTime() - System.currentTimeMillis();
            return (int) (diff / (1000L * 60 * 60 * 24));
        } catch (Exception e) {
            try {
                Date fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(fechaStr);
                if (fecha == null) return 999;
                long diff = fecha.getTime() - System.currentTimeMillis();
                return (int) (diff / (1000L * 60 * 60 * 24));
            } catch (Exception ignored) {
                return 999;
            }
        }
    }

    private void aplicarIndicador(VH h, int dias) {
        int color;
        String etiqueta;

        if (dias <= 0) {
            color    = Color.parseColor("#B71C1C");
            etiqueta = "¡Hoy!";
        } else if (dias <= 7) {
            color    = Color.parseColor("#F44336");
            etiqueta = dias + "d";
        } else if (dias <= 30) {
            color    = Color.parseColor("#FF9800");
            etiqueta = dias + "d";
        } else {
            color    = Color.parseColor("#4CAF50");
            etiqueta = dias + "d";
        }

        h.barraUrgencia.setBackgroundColor(color);
        h.txtDias.setBackgroundColor(color);
        h.txtDias.setText(etiqueta);
    }

    private String formatearFechaProxima(String fechaStr) {
        if (fechaStr == null || fechaStr.isEmpty()) return "Fecha no disponible";
        try {
            String limpia = fechaStr.split("\\.")[0].replace("Z", "");
            Date fecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(limpia);
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha);
        } catch (Exception e) {
            try {
                Date fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(fechaStr);
                return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha);
            } catch (Exception ignored) {
                return fechaStr;
            }
        }
    }

    static class VH extends RecyclerView.ViewHolder {
        View     barraUrgencia;
        TextView txtVacuna, txtMiembro, txtFecha, txtDias;

        VH(@NonNull View v) {
            super(v);
            barraUrgencia = v.findViewById(R.id.barraUrgencia);
            txtVacuna     = v.findViewById(R.id.txtVacunaNombre);
            txtMiembro    = v.findViewById(R.id.txtMiembro);
            txtFecha      = v.findViewById(R.id.txtFecha);
            txtDias       = v.findViewById(R.id.txtDiasRestantes);
        }
    }
}
