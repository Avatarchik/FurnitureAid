/*
 * Decompiled with CFR 0_118.
 */
package com.vuforia;

import com.vuforia.Marker;
import com.vuforia.Trackable;
import com.vuforia.TrackableResult;
import com.vuforia.Type;
import com.vuforia.VuforiaJNI;

public class MarkerResult
extends TrackableResult {
    private long swigCPtr;

    protected MarkerResult(long cPtr, boolean cMemoryOwn) {
        super(VuforiaJNI.MarkerResult_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(MarkerResult obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    @Override
    protected void finalize() {
        this.delete();
    }

    @Override
    protected synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                VuforiaJNI.delete_MarkerResult(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    @Override
    public boolean equals(Object obj) {
        boolean equal = false;
        if (obj instanceof MarkerResult) {
            equal = ((MarkerResult)obj).swigCPtr == this.swigCPtr;
        }
        return equal;
    }

    public static Type getClassType() {
        return new Type(VuforiaJNI.MarkerResult_getClassType(), true);
    }

    @Override
    public Trackable getTrackable() {
        return new Marker(VuforiaJNI.MarkerResult_getTrackable(this.swigCPtr, this), false);
    }
}

