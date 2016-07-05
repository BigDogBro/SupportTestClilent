package com.example.meizu.supporttestclilent;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Date;

/**
 * Created by meizu on 2016/7/2.
 */
public class LongRunService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                Log.d("LongRunService","excuted at" +new Date().toString());
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int tens = 10 * 1000;
        long time = SystemClock.elapsedRealtime() + tens;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0 ,i ,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, time, pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
