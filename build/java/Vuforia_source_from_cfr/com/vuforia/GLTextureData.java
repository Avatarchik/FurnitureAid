/*
 * Decompiled with CFR 0_118.
 */
package com.vuforia;

import com.vuforia.TextureData;
import com.vuforia.VuforiaJNI;

public class GLTextureData
extends TextureData {
    private long swigCPtr;

    protected GLTextureData(long cPtr, boolean cMemoryOwn) {
        super(VuforiaJNI.GLTextureData_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(GLTextureData obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    @Override
    protected synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                VuforiaJNI.delete_GLTextureData(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    @Override
    public boolean equals(Object obj) {
        boolean equal = false;
        if (obj instanceof GLTextureData) {
            equal = ((GLTextureData)obj).swigCPtr == this.swigCPtr;
        }
        return equal;
    }

    public GLTextureData(int videoBackgroundTextureID) {
        this(VuforiaJNI.new_GLTextureData__SWIG_0(videoBackgroundTextureID), true);
    }

    public GLTextureData() {
        this(VuforiaJNI.new_GLTextureData__SWIG_1(), true);
    }

    public void setVideoBackgroundTextureID(int value) {
        VuforiaJNI.GLTextureData_VideoBackgroundTextureID_set(this.swigCPtr, this, value);
    }

    public int getVideoBackgroundTextureID() {
        return VuforiaJNI.GLTextureData_VideoBackgroundTextureID_get(this.swigCPtr, this);
    }
}

