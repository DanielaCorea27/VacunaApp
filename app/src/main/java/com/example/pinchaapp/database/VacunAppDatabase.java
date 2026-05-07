package com.example.pinchaapp.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pinchaapp.database.dao.UsuarioDao;
import com.example.pinchaapp.database.entities.Usuario;

@Database(entities = {Usuario.class}, version = 1, exportSchema = false)
public abstract class VacunAppDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();

    // para que solo exista una instancia de la BD
    private static VacunAppDatabase instancia;

    public static VacunAppDatabase getInstance(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(
                    context.getApplicationContext(),
                    VacunAppDatabase.class,
                    "vacunapp_db"
            ).build();
        }
        return instancia;
    }
}