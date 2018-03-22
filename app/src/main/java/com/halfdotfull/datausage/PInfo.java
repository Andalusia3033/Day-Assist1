package com.halfdotfull.datausage;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by nexflare on 22/03/18.
 */

public class PInfo {
    String appname = "";
    String pname = "";
    String versionName = "";
    int versionCode = 0;
    Drawable icon;
    void prettyPrint() {
        Log.d("TAGGER",appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
    }
}