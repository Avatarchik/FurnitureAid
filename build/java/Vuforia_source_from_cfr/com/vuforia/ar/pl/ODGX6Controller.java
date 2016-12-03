/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.view.Surface
 *  android.view.SurfaceHolder
 *  android.view.SurfaceView
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.Window
 *  com.ti.s3d.S3DView
 *  com.ti.s3d.S3DView$Layout
 *  com.ti.s3d.S3DView$RenderMode
 */
package com.vuforia.ar.pl;

import android.app.Activity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.ti.s3d.S3DView;
import com.vuforia.ar.pl.SystemTools;

public class ODGX6Controller {
    private static final String MODULENAME = "ODGX6Controller";
    private boolean stereoEnabled = false;

    public boolean setStereo(boolean enable) {
        SurfaceView sv = this.getSurfaceView();
        if (sv == null) {
            return false;
        }
        SurfaceHolder sh = sv.getHolder();
        if (enable) {
            S3DView.configureSurface((SurfaceHolder)sh, (S3DView.Layout)S3DView.Layout.SIDE_BY_SIDE_LR, (S3DView.RenderMode)S3DView.RenderMode.STEREO);
        } else {
            S3DView.configureSurface((SurfaceHolder)sh, (S3DView.Layout)S3DView.Layout.MONO, (S3DView.RenderMode)S3DView.RenderMode.MONO_LEFT);
        }
        this.stereoEnabled = enable;
        return true;
    }

    public boolean getStereo() {
        return this.stereoEnabled;
    }

    private SurfaceView getSurfaceView() {
        Activity activity = SystemTools.getActivityFromNative();
        ViewGroup vg = (ViewGroup)activity.getWindow().getDecorView();
        return this.findSurfaceView(vg);
    }

    private SurfaceView findSurfaceView(ViewGroup vg) {
        int numChildren = vg.getChildCount();
        for (int i = 0; i < numChildren; ++i) {
            View v = vg.getChildAt(i);
            if (v instanceof SurfaceView) {
                SurfaceView sv = (SurfaceView)v;
                if (!sv.getHolder().getSurface().isValid()) continue;
                return sv;
            }
            if (!(v instanceof ViewGroup)) continue;
            return this.findSurfaceView((ViewGroup)v);
        }
        return null;
    }
}

