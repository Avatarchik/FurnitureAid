/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.util.DisplayMetrics
 *  android.view.Surface
 *  android.view.SurfaceHolder
 *  android.view.SurfaceView
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.Window
 *  com.osterhoutgroup.api.ext.ExtendDisplay
 */
package com.vuforia.ar.pl;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.osterhoutgroup.api.ext.ExtendDisplay;
import com.vuforia.ar.pl.SystemTools;

public class ODGR7Controller {
    private static final String MODULENAME = "ODGR7Controller";
    private boolean stereoEnabled = false;

    private void logMetrics(String when, Activity activity, Window w) {
        DisplayMetrics metrics = new DisplayMetrics();
        ExtendDisplay.getDisplayMetrics((Context)activity, (Window)w, (DisplayMetrics)metrics);
        int mScreenWidth = metrics.widthPixels;
        int mScreenHeight = metrics.heightPixels;
    }

    public boolean setStereo(final boolean enable) {
        final Activity activity = SystemTools.getActivityFromNative();
        if (activity == null) {
            return false;
        }
        activity.runOnUiThread(new Runnable(){

            @Override
            public void run() {
                ODGR7Controller.this.doSetStereo(activity, enable);
            }
        });
        return true;
    }

    private void doSetStereo(Activity activity, boolean enable) {
        Window window = activity.getWindow();
        if (window != null) {
            this.logMetrics("Before extendWindow", activity, window);
            ExtendDisplay.extendWindow((Window)window, (boolean)enable);
            this.logMetrics("After extendWindow", activity, window);
            this.setStereoSurfaces(window, enable);
            this.stereoEnabled = enable;
        }
    }

    public boolean getStereo() {
        return this.stereoEnabled;
    }

    private void setStereoSurfaces(Window window, boolean enable) {
        ViewGroup vg = (ViewGroup)window.getDecorView();
        this.configureSurfaceViews(vg, enable);
    }

    private void configureSurfaceViews(ViewGroup vg, boolean enable) {
        int numChildren = vg.getChildCount();
        for (int i = 0; i < numChildren; ++i) {
            View v = vg.getChildAt(i);
            if (v instanceof SurfaceView) {
                SurfaceView sv = (SurfaceView)v;
                if (!sv.getHolder().getSurface().isValid()) continue;
                ExtendDisplay.extendSurface((SurfaceView)sv, (boolean)enable);
                continue;
            }
            if (!(v instanceof ViewGroup)) continue;
            this.configureSurfaceViews((ViewGroup)v, enable);
        }
    }

}

