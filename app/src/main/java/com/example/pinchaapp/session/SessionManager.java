package com.example.pinchaapp.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREFS_NAME   = "ApiVacunas";
    private static final String KEY_TOKEN    = "jwt_token";
    private static final String KEY_ID       = "id_usuario";
    private static final String KEY_NOMBRE   = "nombre_usuario";
    private static final String KEY_CORREO   = "correo_usuario";
    private static final String KEY_ROL      = "rol_usuario";

    private static SharedPreferences prefs;

    // Inicializar el SharedPreferences
    public static void init(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Guarda la sesion completa al hacer login
    public static void guardarSesion(String token, int idUsuario,
                                     String nombre, String correo, String rol) {
        prefs.edit()
                .putString(KEY_TOKEN,  token)
                .putInt(KEY_ID,        idUsuario)
                .putString(KEY_NOMBRE, nombre)
                .putString(KEY_CORREO, correo)
                .putString(KEY_ROL,    rol)
                .apply();
    }

    // Getters
    public static String getToken() {
        return prefs.getString(KEY_TOKEN, "");
    }

    public static int getIdUsuario() {
        return prefs.getInt(KEY_ID, -1);
    }

    public static String getNombre() {
        return prefs.getString(KEY_NOMBRE, "");
    }

    public static String getCorreo() {
        return prefs.getString(KEY_CORREO, "");
    }

    public static String getRol() {
        return prefs.getString(KEY_ROL, "");
    }

    // Checa si hay sesion activa
    public static boolean haySesion() {
        return !getToken().isEmpty();
    }

    // Cerrar sesion
    public static void cerrarSesion() {
        prefs.edit().clear().apply();
    }
}