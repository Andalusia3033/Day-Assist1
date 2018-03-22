package com.halfdotfull.datausage;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class ApplicationItem {
    private long tx = 0;
    private long rx = 0;

    private long wifi_tx = 0;
    private long wifi_rx = 0;

    private long mobil_tx = 0;
    private long mobil_rx = 0;

    private long current_tx = 0;
    private long current_rx = 0;

    private ApplicationInfo app;

    private boolean isMobil = false;

    public ApplicationItem(ApplicationInfo _app) {
        app = _app;
        update();
    }

    public void update() {
        long delta_tx = getDataUsage(app.uid) - tx;
        long delta_rx = getDataUsage(app.uid) - rx;

        tx =  getDataUsage(app.uid);
        rx = getDataUsage(app.uid);

        current_tx = current_tx + delta_tx;
        current_rx = current_rx + delta_rx;

        if(isMobil == true) {
            mobil_tx = mobil_tx + delta_tx;
            mobil_rx = mobil_rx + delta_rx;
        } else {
            wifi_tx = wifi_tx + delta_tx;
            wifi_rx = wifi_rx + delta_rx;
        }
    }

    public static ApplicationItem create(ApplicationInfo _app){
        long _tx = TrafficStats.getUidTxBytes(_app.uid);
        long _rx = TrafficStats.getUidRxBytes(_app.uid);
        Log.d("TAGGER ", "create: "+ _tx+" "+_rx);
        File dir = new File("/proc/uid_stat/");
        String[] children = dir.list();
        if(!Arrays.asList(children).contains(String.valueOf(_app.uid))){
            return null;
        }
        File uidFileDir = new File("/proc/uid_stat/"+String.valueOf(_app.uid));
        File uidActualFileReceived = new File(uidFileDir,"tcp_rcv");
        File uidActualFileSent = new File(uidFileDir,"tcp_snd");

        String textReceived = "0";
        String textSent = "0";

        try {
            BufferedReader brReceived = new BufferedReader(new FileReader(uidActualFileReceived));
            BufferedReader brSent = new BufferedReader(new FileReader(uidActualFileSent));
            String receivedLine;
            String sentLine;

            if ((receivedLine = brReceived.readLine()) != null) {
                textReceived = receivedLine;
            }
            if ((sentLine = brSent.readLine()) != null) {
                textSent = sentLine;
            }

        }
        catch (IOException e) {

        }
        Log.d("TAGGERUS", "create: "+(Long.valueOf(textReceived) + Long.valueOf(textSent)));

        if( (Long.valueOf(textReceived) + Long.valueOf(textSent)) > 0) return new ApplicationItem(_app);
        return null;
    }
    public Long getDataUsage(int uid){
        long _tx = TrafficStats.getUidTxBytes(uid);
        long _rx = TrafficStats.getUidRxBytes(uid);
        Log.d("TAGGER ", "create: "+ _tx+" "+_rx);
        File dir = new File("/proc/uid_stat/");
        String[] children = dir.list();
        if(!Arrays.asList(children).contains(String.valueOf(uid))){
            return Long.valueOf(0);
        }
        File uidFileDir = new File("/proc/uid_stat/"+String.valueOf(uid));
        File uidActualFileReceived = new File(uidFileDir,"tcp_rcv");
        File uidActualFileSent = new File(uidFileDir,"tcp_snd");

        String textReceived = "0";
        String textSent = "0";

        try {
            BufferedReader brReceived = new BufferedReader(new FileReader(uidActualFileReceived));
            BufferedReader brSent = new BufferedReader(new FileReader(uidActualFileSent));
            String receivedLine;
            String sentLine;

            if ((receivedLine = brReceived.readLine()) != null) {
                textReceived = receivedLine;
            }
            if ((sentLine = brSent.readLine()) != null) {
                textSent = sentLine;
            }

        }
        catch (IOException e) {

        }
        Log.d("TAGGERUS", "create: "+(Long.valueOf(textReceived) + Long.valueOf(textSent)));

        if( (Long.valueOf(textReceived) + Long.valueOf(textSent)) > 0) return (Long.valueOf(textReceived) + Long.valueOf(textSent));
        return Long.valueOf(0);
    }

    public int getTotalUsageKb() {
        return Math.round((tx + rx)/ 1024);
    }

    public String getApplicationLabel(PackageManager _packageManager) {
        return _packageManager.getApplicationLabel(app).toString();
    }

    public Drawable getIcon(PackageManager _packageManager) {
        return _packageManager.getApplicationIcon(app);
    }

    public void setMobilTraffic(boolean _isMobil) {
        isMobil = _isMobil;
    }
}
