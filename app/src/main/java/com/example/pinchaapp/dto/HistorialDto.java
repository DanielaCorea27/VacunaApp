package com.example.pinchaapp.dto;

import java.util.Date;

public class HistorialDto {

    // ============================================================
    // REQUEST DTOs
    // ============================================================
    public static class RegistrarVacunacionDto {
        private int idMiembro;
        private int idVacuna;
        private Integer idCentro;
        private String fechaAplicacion;
        private int dosisNumero;
        private String lote;
        private String nombreMedico;
        private String observaciones;

        public RegistrarVacunacionDto() {}

        // Getters y Setters
        public int getIdMiembro() { return idMiembro; }
        public void setIdMiembro(int idMiembro) { this.idMiembro = idMiembro; }
        public int getIdVacuna() { return idVacuna; }
        public void setIdVacuna(int idVacuna) { this.idVacuna = idVacuna; }
        public Integer getIdCentro() { return idCentro; }
        public void setIdCentro(Integer idCentro) { this.idCentro = idCentro; }
        public String getFechaAplicacion() { return fechaAplicacion; }
        public void setFechaAplicacion(String fechaAplicacion) { this.fechaAplicacion = fechaAplicacion; }
        public int getDosisNumero() { return dosisNumero; }
        public void setDosisNumero(int dosisNumero) { this.dosisNumero = dosisNumero; }
        public String getLote() { return lote; }
        public void setLote(String lote) { this.lote = lote; }
        public String getNombreMedico() { return nombreMedico; }
        public void setNombreMedico(String nombreMedico) { this.nombreMedico = nombreMedico; }
        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    }

    // ============================================================
    // RESPONSE DTOs
    // ============================================================
    public static class HistorialResponseDto {
        private int id;
        private String fechaAplicacion;
        private String proximaDosis;
        private int dosisNumero;
        private int totalDosis;
        private String lote;
        private String nombreMedico;
        private String observaciones;
        private String vacuna;
        private String fabricante;
        private String tipoVacuna;
        private String centro;
        private String centroDireccion;
        private String recordatorioEstado;
        private String fechaRecordatorio;

        public HistorialResponseDto() {}

        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getFechaAplicacion() { return fechaAplicacion; }
        public void setFechaAplicacion(String fechaAplicacion) { this.fechaAplicacion = fechaAplicacion; }
        public String getProximaDosis() { return proximaDosis; }
        public void setProximaDosis(String proximaDosis) { this.proximaDosis = proximaDosis; }
        public int getDosisNumero() { return dosisNumero; }
        public void setDosisNumero(int dosisNumero) { this.dosisNumero = dosisNumero; }
        public int getTotalDosis() {return  totalDosis; }
        public void setTotalDosis(int totalDosis) {this.totalDosis = totalDosis; }
        public String getLote() { return lote; }
        public void setLote(String lote) { this.lote = lote; }
        public String getNombreMedico() { return nombreMedico; }
        public void setNombreMedico(String nombreMedico) { this.nombreMedico = nombreMedico; }
        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
        public String getVacuna() { return vacuna; }
        public void setVacuna(String vacuna) { this.vacuna = vacuna; }
        public String getFabricante() { return fabricante; }
        public void setFabricante(String fabricante) { this.fabricante = fabricante; }
        public String getTipoVacuna() { return tipoVacuna; }
        public void setTipoVacuna(String tipoVacuna) { this.tipoVacuna = tipoVacuna; }
        public String getCentro() { return centro; }
        public void setCentro(String centro) { this.centro = centro; }
        public String getCentroDireccion() { return centroDireccion; }
        public void setCentroDireccion(String centroDireccion) { this.centroDireccion = centroDireccion; }
        public String getRecordatorioEstado() { return recordatorioEstado; }
        public void setRecordatorioEstado(String recordatorioEstado) { this.recordatorioEstado = recordatorioEstado; }
        public String getFechaRecordatorio() { return fechaRecordatorio; }
        public void setFechaRecordatorio(String fechaRecordatorio) { this.fechaRecordatorio = fechaRecordatorio; }
    }

    public static class ProximaDosisResponseDto {
        private String miembro;
        private String tipoMiembro;
        private String vacuna;
        private int dosisAplicada;
        private String proximaDosis;
        private Integer diasRestantes;
        private String recordatorio;

        public ProximaDosisResponseDto() {}

        // Getters y Setters
        public String getMiembro() { return miembro; }
        public void setMiembro(String miembro) { this.miembro = miembro; }
        public String getTipoMiembro() { return tipoMiembro; }
        public void setTipoMiembro(String tipoMiembro) { this.tipoMiembro = tipoMiembro; }
        public String getVacuna() { return vacuna; }
        public void setVacuna(String vacuna) { this.vacuna = vacuna; }
        public int getDosisAplicada() { return dosisAplicada; }
        public void setDosisAplicada(int dosisAplicada) { this.dosisAplicada = dosisAplicada; }
        public String getProximaDosis() { return proximaDosis; }
        public void setProximaDosis(String proximaDosis) { this.proximaDosis = proximaDosis; }
        public Integer getDiasRestantes() { return diasRestantes; }
        public void setDiasRestantes(Integer diasRestantes) { this.diasRestantes = diasRestantes; }
        public String getRecordatorio() { return recordatorio; }
        public void setRecordatorio(String recordatorio) { this.recordatorio = recordatorio; }
    }
    // ============================================================
    // DTO EXCLUSIVO PARA ADAPTADOR DE DETALLE (UI CONTROL)
    // ============================================================
    public static class VacunaHistorialDto {
        private int id;
        private String nombreVacuna;
        private int dosisNumero;
        private int totalDosis;
        private boolean aplicada;
        private String fechaAplicacion;
        private String proximaDosis;

        public VacunaHistorialDto() {}

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getNombreVacuna() { return nombreVacuna; }
        public void setNombreVacuna(String nombreVacuna) { this.nombreVacuna = nombreVacuna; }
        public int getDosisNumero() { return dosisNumero; }
        public void setDosisNumero(int dosisNumero) { this.dosisNumero = dosisNumero; }
        public int getTotalDosis() { return totalDosis; }
        public void setTotalDosis(int totalDosis) { this.totalDosis = totalDosis; }
        public boolean isAplicada() { return aplicada; }
        public void setAplicada(boolean aplicada) { this.aplicada = aplicada; }
        public String getFechaAplicacion() { return fechaAplicacion; }
        public void setFechaAplicacion(String fechaAplicacion) { this.fechaAplicacion = fechaAplicacion; }
        public String getProximaDosis() { return proximaDosis; }
        public void setProximaDosis(String proximaDosis) { this.proximaDosis = proximaDosis; }
    }
}