/*
 * Decompiled with CFR 0_118.
 */
package com.vuforia;

import com.vuforia.VuforiaJNI;

public class CameraField {
    private long swigCPtr;
    protected boolean swigCMemOwn;

    protected CameraField(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(CameraField obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    protected synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                VuforiaJNI.delete_CameraField(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public boolean equals(Object obj) {
        boolean equal = false;
        if (obj instanceof CameraField) {
            equal = ((CameraField)obj).swigCPtr == this.swigCPtr;
        }
        return equal;
    }

    public CameraField() {
        this(VuforiaJNI.new_CameraField(), true);
    }

    public int getType() {
        return VuforiaJNI.CameraField_Type_get(this.swigCPtr, this);
    }

    public String getKey() {
        return VuforiaJNI.CameraField_Key_get(this.swigCPtr, this);
    }

    public static final class DataType {
        public static final int TypeString = 0;
        public static final int TypeInt64 = 1;
        public static final int TypeFloat = 2;
        public static final int TypeBool = 3;
        public static final int TypeInt64Range = 4;
        public static final int TypeUnknown = 5;

        private DataType() {
        }
    }

}

