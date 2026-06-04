package com.example.pinchaapp.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.pinchaapp.ProximasDosisActivity;
import com.example.pinchaapp.R;

public class VacunaReminderWorker extends Worker {

    public static final String CHANNEL_ID    = "vacuna_recordatorio";
    public static final String KEY_MIEMBRO   = "miembro";
    public static final String KEY_VACUNA    = "vacuna";
    public static final String KEY_FECHA     = "fecha";
    public static final String KEY_ID_PERFIL = "idPerfil";

    public VacunaReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        String miembro = getInputData().getString(KEY_MIEMBRO);
        String vacuna  = getInputData().getString(KEY_VACUNA);
        String fecha   = getInputData().getString(KEY_FECHA);

        crearCanalSiNecesario();
        mostrarNotificacion(miembro, vacuna, fecha);
        return Result.success();
    }

    private void crearCanalSiNecesario() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    CHANNEL_ID,
                    "Recordatorio de vacunas",
                    NotificationManager.IMPORTANCE_HIGH);
            canal.setDescription("Recordatorios de próximas dosis de vacunas");
            obtenerManager().createNotificationChannel(canal);
        }
    }

    private void mostrarNotificacion(String miembro, String vacuna, String fecha) {
        String titulo = "Vacuna pendiente: " + (vacuna != null ? vacuna : "próxima dosis");
        String cuerpo = (miembro != null ? miembro : "Un miembro de la familia")
                + " tiene una dosis programada"
                + (fecha != null ? " para el " + fecha : "");

        int idPerfil = getInputData().getInt(KEY_ID_PERFIL, 0);

        Intent abrirApp = new Intent(getApplicationContext(), ProximasDosisActivity.class);
        abrirApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        abrirApp.putExtra("idPerfil", idPerfil);
        abrirApp.putExtra("nombre",   miembro);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, abrirApp,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notificacion)
                .setContentTitle(titulo)
                .setContentText(cuerpo)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(cuerpo))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        obtenerManager().notify((int) System.currentTimeMillis(), builder.build());
    }

    private NotificationManager obtenerManager() {
        return (NotificationManager)
                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
