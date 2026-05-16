package com.example.pinchaapp.dto;

public class LoginDto {

    private String correo;
    private String password;

    public LoginDto(String correo, String password) {
        this.correo   = correo;
        this.password = password;
    }

    public String getCorreo()            { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getPassword()              { return password; }
    public void setPassword(String password) { this.password = password; }
}
