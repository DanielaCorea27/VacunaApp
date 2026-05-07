package com.example.pinchaapp.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "usuarios")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "apellido")
    private String apellido;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "telefono")
    private String telefono;

    @ColumnInfo(name = "password")
    private String password;

    // Constructor
    public Usuario(String nombre, String apellido, String email, String telefono, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.password = password;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String n) {
        this.nombre = n;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String a) {
        this.apellido = a;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String e) {
        this.email = e;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String t) {
        this.telefono = t;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String p) {
        this.password = p;
    }
}