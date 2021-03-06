/*
 * Decompiled with CFR 0_118.
 */
package com.vuforia;

import com.vuforia.Box3D;
import com.vuforia.CylinderTarget;
import com.vuforia.DeviceTrackable;
import com.vuforia.ImageTarget;
import com.vuforia.Marker;
import com.vuforia.Matrix34F;
import com.vuforia.MultiTarget;
import com.vuforia.ObjectTarget;
import com.vuforia.Prop;
import com.vuforia.Reconstruction;
import com.vuforia.Surface;
import com.vuforia.Trackable;
import com.vuforia.Type;
import com.vuforia.VuMarkTarget;
import com.vuforia.VuMarkTemplate;
import com.vuforia.VuforiaJNI;
import com.vuforia.Word;

public class ReconstructionFromTarget
extends Reconstruction {
    private long swigCPtr;

    protected ReconstructionFromTarget(long cPtr, boolean cMemoryOwn) {
        super(VuforiaJNI.ReconstructionFromTarget_SWIGUpcast(cPtr), cMemoryOwn);
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(ReconstructionFromTarget obj) {
        return obj == null ? 0 : obj.swigCPtr;
    }

    @Override
    protected synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                throw new UnsupportedOperationException("C++ destructor does not have public access");
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    @Override
    public boolean equals(Object obj) {
        boolean equal = false;
        if (obj instanceof ReconstructionFromTarget) {
            equal = ((ReconstructionFromTarget)obj).swigCPtr == this.swigCPtr;
        }
        return equal;
    }

    public static Type getClassType() {
        return new Type(VuforiaJNI.ReconstructionFromTarget_getClassType(), true);
    }

    public boolean setInitializationTarget(Trackable trackable, Box3D occluderVolume) {
        return VuforiaJNI.ReconstructionFromTarget_setInitializationTarget__SWIG_0(this.swigCPtr, this, Trackable.getCPtr(trackable), trackable, Box3D.getCPtr(occluderVolume), occluderVolume);
    }

    public boolean setInitializationTarget(Trackable trackable, Box3D occluderVolume, Matrix34F offsetToOccluderPose) {
        return VuforiaJNI.ReconstructionFromTarget_setInitializationTarget__SWIG_1(this.swigCPtr, this, Trackable.getCPtr(trackable), trackable, Box3D.getCPtr(occluderVolume), occluderVolume, Matrix34F.getCPtr(offsetToOccluderPose), offsetToOccluderPose);
    }

    public Trackable getInitializationTarget() {
        long cPtr = VuforiaJNI.ReconstructionFromTarget_getInitializationTarget(this.swigCPtr, this);
        if (cPtr == 0) {
            return null;
        }
        Trackable tmp = new Trackable(cPtr, false);
        if (tmp.isOfType(ImageTarget.getClassType())) {
            return new ImageTarget(cPtr, false);
        }
        if (tmp.isOfType(CylinderTarget.getClassType())) {
            return new CylinderTarget(cPtr, false);
        }
        if (tmp.isOfType(MultiTarget.getClassType())) {
            return new MultiTarget(cPtr, false);
        }
        if (tmp.isOfType(VuMarkTarget.getClassType())) {
            return new VuMarkTarget(cPtr, false);
        }
        if (tmp.isOfType(VuMarkTemplate.getClassType())) {
            return new VuMarkTemplate(cPtr, false);
        }
        if (tmp.isOfType(ObjectTarget.getClassType())) {
            return new ObjectTarget(cPtr, false);
        }
        if (tmp.isOfType(Word.getClassType())) {
            return new Word(cPtr, false);
        }
        if (tmp.isOfType(Marker.getClassType())) {
            return new Marker(cPtr, false);
        }
        if (tmp.isOfType(Surface.getClassType())) {
            return new Surface(cPtr, false);
        }
        if (tmp.isOfType(Prop.getClassType())) {
            return new Prop(cPtr, false);
        }
        if (tmp.isOfType(DeviceTrackable.getClassType())) {
            return new DeviceTrackable(cPtr, false);
        }
        return null;
    }
}

