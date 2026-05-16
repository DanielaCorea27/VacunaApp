package com.example.pinchaapp.dto;

public class AuthResponseDto {

    private String token;
    private String nombre;
    private String correo;
    private String rol;
    private int    idUsuario;

    public String getToken()           { return token; }
    public void setToken(String token) { this.token = token; }

    public String getNombre()            { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo()            { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getRol()         { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public int getIdUsuario()              { return idUsuario; }
    public void setIdUsuario(int idUsuario){ this.idUsuario = idUsuario; }
}
