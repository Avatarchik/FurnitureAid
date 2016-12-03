/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.res.AssetManager
 *  android.os.Environment
 */
package com.vuforia.ar.pl;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Environment;
import com.vuforia.ar.pl.SystemTools;
import java.io.File;

public class FileTools {
    private static final int FILE_PATHTYPEINDEX_ABSOLUTE = -1;
    private static final int FILE_PATHTYPEINDEX_ANDROID_ASSETS = 0;
    private static final int FILE_PATHTYPEINDEX_ANDROID_PRIVATEAPPSTORAGE = 1;
    private static final int FILE_PATHTYPEINDEX_ANDROID_PRIVATEAPPCACHE = 2;
    private static final int FILE_PATHTYPEINDEX_ANDROID_MEDIASTORAGE = 3;
    private static final int FILE_PATHTYPEINDEX_ANDROID_DATALOCAL = 4;
    private static final String MODULENAME = "FileTools";

    public static String getAbsolutePath(int pathTypeIndex, String relativePath) {
        String basePath = null;
        switch (pathTypeIndex) {
            case 1: {
                Activity activity = SystemTools.getActivityFromNative();
                if (activity == null) {
                    SystemTools.setSystemErrorCode(6);
                    return null;
                }
                File fileDir = activity.getFilesDir();
                if (fileDir == null) {
                    SystemTools.setSystemErrorCode(6);
                    return null;
                }
                basePath = fileDir.getAbsolutePath();
                break;
            }
            case 2: {
                Activity activity = SystemTools.getActivityFromNative();
                if (activity == null) {
                    SystemTools.setSystemErrorCode(6);
                    return null;
                }
                File cacheDir = activity.getCacheDir();
                if (cacheDir == null) {
                    SystemTools.setSystemErrorCode(6);
                    return null;
                }
                basePath = cacheDir.getAbsolutePath();
                break;
            }
            case 3: {
                File externalStorageDir = Environment.getExternalStorageDirectory();
                if (externalStorageDir == null) {
                    SystemTools.setSystemErrorCode(6);
                    return null;
                }
                basePath = externalStorageDir.getAbsolutePath();
                break;
            }
            default: {
                SystemTools.setSystemErrorCode(6);
                return null;
            }
        }
        if (basePath != null && relativePath != null) {
            if (basePath.length() > 0 && basePath.charAt(basePath.length() - 1) != '/') {
                basePath = basePath + "/";
            }
            basePath = basePath + relativePath;
        }
        return basePath;
    }

    public static boolean mediastorage_isAvailable() {
        String state = Environment.getExternalStorageState();
        return "mounted".equals(state) || "mounted_ro".equals(state);
    }

    public static AssetManager get_assetmanager() {
        Activity activity = SystemTools.getActivityFromNative();
        if (activity == null) {
            SystemTools.setSystemErrorCode(6);
            return null;
        }
        return activity.getAssets();
    }
}

