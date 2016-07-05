package com.example.meizu.supporttestclilent.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.example.meizu.supporttestclilent.service.WifiConnect;
import com.example.meizu.supporttestclilent.service.WifiLockService;

import java.util.List;

/**
 * Created by meizu on 2016/7/4.
 */
public class wifiSwitchReceiver extends BroadcastReceiver{

    private WifiManager wifiManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("WifiLock", "receive...");
        String flag = intent.getStringExtra("wifiName");
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiLockService wifilockService = new WifiLockService(context);
        if (flag.equals("MZ-Inweb-Test")){
            wifilockService.connectToNet("MZ-Inweb-Test","","Inweb@meizu.com");
            Toast.makeText(context,"Switch MZ-Inweb-Test Success",Toast.LENGTH_SHORT).show();
        }
        else if (flag.equals("MZ-MEIZU-5G")) {
            wifilockService.connectToNet("MZ-MEIZU-5G","wenli","!WEN120877");
            Toast.makeText(context,"Switch MZ-MEIZU-5G Success",Toast.LENGTH_SHORT).show();
        }
        Log.d("WifiLock", "finish...");
    }

}
