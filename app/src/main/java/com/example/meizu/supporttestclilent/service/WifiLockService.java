package com.example.meizu.supporttestclilent.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;


/**
 * Author: jinghao
 * Date: 2015-04-10
 */
public class WifiLockService {
    public static WifiManager mWifiManager;
    public static Context appContext;
    public WifiLockService(Context context){
        this.appContext = context;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    // 清除除ssid的其他wifi
    public void clearWifiConfig(String ssid) {
        try {
            List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();
            if (null == configs) {
                return;
            }
            for (WifiConfiguration config : configs) {
                mWifiManager.removeNetwork(config.networkId);
            }
            mWifiManager.saveConfiguration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectToNet(String ssid, String usr, String pwd) {
        this.clearWifiConfig(ssid);
        while (!("\"" + ssid + "\"")
                .equals(mWifiManager.getConnectionInfo().getSSID())
                || !this.isWifiConnected()
                || !this.mWifiManager.isWifiEnabled()) {

            WifiConfiguration config;
            if ("".equals(ssid)) {
                Log.e("", "ssid is invalid, do nothing");
                break;
            }
            if (isEapWifi(ssid)) {
                config = getEAPConfig(ssid, usr, pwd);
            } else {
                config = getWPAConfig(ssid, pwd);
            }
            int netId = this.mWifiManager.addNetwork(config);
            this.mWifiManager.saveConfiguration();
            this.mWifiManager.disconnect();
            this.mWifiManager.enableNetwork(netId, true);
            this.mWifiManager.reconnect();
            this.mWifiManager.setWifiEnabled(true);
            try {
                Log.e("Service.WifiLock", "connect to wifi...");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isWifiConnected() {
        return isWifiConnected(appContext);
    }

    private boolean isEapWifi(String ssid) {
        return "MZ-MEIZU-5G".equals(ssid) || "MZ-MEIZU-2.4G".equals(ssid) || "MZ-Hkline-5G".equals(ssid);
    }


    public static WifiConfiguration getEAPConfig(String ssid, String usr, String pwd) {
        WifiEnterpriseConfig enterpriseConfig = new WifiEnterpriseConfig();
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = "\"" + ssid + "\"";
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
        enterpriseConfig.setIdentity(usr);
        enterpriseConfig.setPassword(pwd);
        enterpriseConfig.setEapMethod(WifiEnterpriseConfig.Eap.PEAP);
        wifiConfig.enterpriseConfig = enterpriseConfig;
        return wifiConfig;
    }

    public static WifiConfiguration getWPAConfig(String ssid, String pwd) {
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\"" + ssid + "\"";
        config.preSharedKey = "\"" + pwd + "\"";
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.status = WifiConfiguration.Status.ENABLED;
        return config;
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }
}



