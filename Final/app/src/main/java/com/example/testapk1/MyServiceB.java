package com.example.testapk1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyServiceB extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String count = intent.getStringExtra("Count");

        String out = "Word said "+count+" times";

        Log.d("service B","starts");






        createNotificationChannel();


        Intent intent1 = new Intent(this, MainActivity.class);

        intent1.setAction("STOP_FOREGROUND_SERVICE_ACTION");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this, "2")
                .setContentText(out)
                .setContentTitle("Done Listening")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();


        startForeground(2, notification);

        Log.d("Service", "Service should start");
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            Log.e("service","createNotififcationChannel");
            NotificationChannel notificationChannel = new NotificationChannel(
                    "2","Vocabury Running", NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
            Log.e("Service","createdNot Channel");
        }
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }
}
