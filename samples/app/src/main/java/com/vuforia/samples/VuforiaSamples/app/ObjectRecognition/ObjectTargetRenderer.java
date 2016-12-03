
/*===============================================================================
Copyright (c) 2016 PTC Inc. All Rights Reserved.

Copyright (c) 2012-2014 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/

        package com.vuforia.samples.VuforiaSamples.app.ObjectRecognition;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.vuforia.Device;
import com.vuforia.Matrix44F;
import com.vuforia.ObjectTarget;
import com.vuforia.ObjectTargetResult;
import com.vuforia.Renderer;
import com.vuforia.State;
import com.vuforia.Tool;
import com.vuforia.Trackable;
import com.vuforia.TrackableResult;
import com.vuforia.Vuforia;
import com.vuforia.samples.SampleApplication.SampleAppRenderer;
import com.vuforia.samples.SampleApplication.SampleAppRendererControl;
import com.vuforia.samples.SampleApplication.SampleApplicationSession;
import com.vuforia.samples.SampleApplication.utils.CubeObject;
import com.vuforia.samples.SampleApplication.utils.CubeShaders;
import com.vuforia.samples.SampleApplication.utils.LoadingDialogHandler;
import com.vuforia.samples.SampleApplication.utils.SampleUtils;
import com.vuforia.samples.SampleApplication.utils.Texture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


// The renderer class for the ImageTargets sample. 
public class ObjectTargetRenderer implements GLSurfaceView.Renderer, SampleAppRendererControl
{
    private static final String LOGTAG = "ObjectTargetRenderer";

    private SampleApplicationSession vuforiaAppSession;
    private ObjectTargets mActivity;
    private SampleAppRenderer mSampleAppRenderer;

    private Vector<Texture> mTextures;
    private int shaderProgramID;
    private int vertexHandle;
    private int textureCoordHandle;
    private int texSampler2DHandle;
    private int mvpMatrixHandle;
    private int opacityHandle;
    private int colorHandle;

    private CubeObject mCubeObject;

    private Renderer mRenderer;

    private boolean mIsActive = false;


    public ObjectTargetRenderer(ObjectTargets activity,
                                SampleApplicationSession session)
    {
        mActivity = activity;
        vuforiaAppSession = session;

        // SampleAppRenderer used to encapsulate the use of RenderingPrimitives setting
        // the device mode AR/VR and stereo mode
        mSampleAppRenderer = new SampleAppRenderer(this, mActivity, Device.MODE.MODE_AR, false, 10f, 5000f);
    }


    // Called to draw the current frame.
    @Override
    public void onDrawFrame(GL10 gl)
    {
        if (!mIsActive)
            return;

        // Call our function to render content from SampleAppRenderer class
        mSampleAppRenderer.render();
    }


    // Called when the surface is created or recreated.
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        Log.d(LOGTAG, "GLRenderer.onSurfaceCreated");

        // Call Vuforia function to (re)initialize rendering after first use
        // or after OpenGL ES context was lost (e.g. after onPause/onResume):
        vuforiaAppSession.onSurfaceCreated();

        mSampleAppRenderer.onSurfaceCreated();
    }


    // Called when the surface changed size.
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        Log.d(LOGTAG, "GLRenderer.onSurfaceChanged");

        // Call Vuforia function to handle render surface size changes:
        vuforiaAppSession.onSurfaceChanged(width, height);

        // RenderingPrimitives to be updated when some rendering change is done
        mSampleAppRenderer.onConfigurationChanged(mIsActive);

        // Init rendering
        initRendering();
    }


    public void setActive(boolean active)
    {
        mIsActive = active;

        if(mIsActive)
            mSampleAppRenderer.configureVideoBackground();
    }


    // Function for initializing the renderer.
    private void initRendering()
    {
        mCubeObject = new CubeObject();

        mRenderer = Renderer.getInstance();

        // Now generate the OpenGL texture objects and add settings
        for (Texture t : mTextures)
        {
            GLES20.glGenTextures(1, t.mTextureID, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, t.mTextureID[0]);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,
                    t.mWidth, t.mHeight, 0, GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE, t.mData);
        }
        SampleUtils.checkGLError("ObjectTarget GLInitRendering");

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, Vuforia.requiresAlpha() ? 0.0f
                : 1.0f);

        shaderProgramID = SampleUtils.createProgramFromShaderSrc(
                CubeShaders.CUBE_MESH_VERTEX_SHADER,
                CubeShaders.CUBE_MESH_FRAGMENT_SHADER);

        vertexHandle = GLES20.glGetAttribLocation(shaderProgramID,
                "vertexPosition");
        textureCoordHandle = GLES20.glGetAttribLocation(shaderProgramID,
                "vertexTexCoord");
        texSampler2DHandle = GLES20.glGetUniformLocation(shaderProgramID,
                "texSampler2D");
        mvpMatrixHandle = GLES20.glGetUniformLocation(shaderProgramID,
                "modelViewProjectionMatrix");
        opacityHandle = GLES20.glGetUniformLocation(shaderProgramID,
                "opacity");
        colorHandle = GLES20.glGetUniformLocation(shaderProgramID, "color");

        // Hide the Loading Dialog
        mActivity.loadingDialogHandler
                .sendEmptyMessage(LoadingDialogHandler.HIDE_LOADING_DIALOG);

    }

    private class Action {
        public String destination;
        public String source;
        public float angleX;
        public float angleY;
        public float angleZ;
        public float offsetX;
        public float offsetY;
        public float offsetZ;

        public Action(String destination, String source, float angleX, float angleY, float angleZ, float offsetX, float offsetY, float offsetZ) {
            this.destination = destination;
            this.source = source;
            this.angleX = angleX;
            this.angleY = angleY;
            this.angleZ = angleZ;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
        }
    }

    static int count = 0;
    ArrayList<Action> actionList = new ArrayList<>(Arrays.asList(
            new Action("luna_bar","annies_bar", 90, 0, 0, 0, 0, 0),
            new Action("box", "luna_bar", 90, 0, 0, 0, 0, 0)
    ));
    static int destID = 1;

    // The render function called from SampleAppRendering by using RenderingPrimitives views.
    // The state is owned by SampleAppRenderer which is controlling it's lifecycle.
    // State should not be cached outside this method.
    public void renderFrame(State state, float[] projectionMatrix)
    {
        // Renders video background replacing Renderer.DrawVideoBackground()
        mSampleAppRenderer.renderVideoBackground();
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        float[] zeroMatrix = null;
        float[] pose = new float[3];
        // did we find any trackables this frame?
        for (int tIdx = 0; tIdx  < state.getNumTrackableResults(); tIdx++)
        {
            TrackableResult result = state.getTrackableResult(tIdx);
            Trackable trackable = result.getTrackable();
            printUserData(trackable);

            if (!result.isOfType(ObjectTargetResult.getClassType()))
                continue;

            ObjectTarget objectTarget = (ObjectTarget) trackable;
            String name = objectTarget.getName();
            Action action = actionList.get(destID - 1);
            Log.d(LOGTAG, "" + destID);

            if (name.equals(action.source) || name.equals(action.destination)) {
                Matrix44F modelViewMatrix_Vuforia = Tool
                        .convertPose2GLMatrix(result.getPose());
                float[] modelViewMatrix = modelViewMatrix_Vuforia.getData();

                // deal with the modelview and projection matrices
                float[] modelViewProjection = new float[16];

                float[] objectSize = objectTarget.getSize().getData();

                Matrix.translateM(modelViewMatrix, 0, objectSize[0]/2, objectSize[1]/2, objectSize[2]/10);
                Matrix.scaleM(modelViewMatrix, 0, objectSize[0]/3, objectSize[1]/5, objectSize[2]/8);
//            if (tIdx == 0){
//                pose[0] = modelViewMatrix[12];
//                pose[1] = modelViewMatrix[13];
//                pose[2] = modelViewMatrix[14];
//            }else {
//                double dist =  Math.sqrt(Math.pow(modelViewMatrix[12] - pose[0], 2) + Math.pow(modelViewMatrix[13] - pose[1], 2) + Math.pow(modelViewMatrix[14] - pose[2], 2));
//                Log.d(LOGTAG, "dist" + dist);
//                if (dist < 50){
//                    Log.d(LOGTAG, "State Reached");
//                }
//            }
                if (name.equals(action.source)) {
                    for (int idDest = 0; idDest < state.getNumTrackableResults(); idDest++) {
                        TrackableResult resultDest = state.getTrackableResult(idDest);
                        ObjectTarget targetDest = (ObjectTarget) resultDest.getTrackable();
                        String nameDest = targetDest.getName();
                        if (nameDest.equals(action.destination)) {
                            float[] modelViewDest = Tool.convertPose2GLMatrix(resultDest.getPose()).getData();
                            float[] objectSizeDest = targetDest.getSize().getData();


//                        float[] rotateRelation = new float[16];
//                    Matrix.setRotateEulerM(rotateRelation, 0, action.angleX, action.angleY, );
//                    Matrix.rotateM(modelViewDest, 0, );
                            Matrix.translateM(modelViewDest, 0, action.offsetX, action.offsetY, action.offsetZ);
                            Matrix.translateM(modelViewDest, 0, objectSizeDest[0] / 2, objectSizeDest[1] / 2, objectSizeDest[2] / 2);
                            Matrix.scaleM(modelViewDest, 0, objectSizeDest[0] / 2, objectSizeDest[1] / 2, objectSizeDest[2] / 2);

                            float mse = 0;
                            for (int i = 12; i < 15; i++) {
                                mse += Math.pow(modelViewDest[i] - modelViewMatrix[i], 2) / 3;
                            }
                            Log.d(LOGTAG, "" + mse);
                            if (mse < 150) {
                                Log.d(LOGTAG, "State Reached");
                                destID = 2;
                            }
                            for (int i = 12; i < 15; i++) {
                                modelViewMatrix[i] = count / 50.0f * modelViewDest[i] + (50.0f - count) / 50.0f * modelViewMatrix[i];
                            }
                            break;
                        }
                    }
                }

                Matrix.multiplyMM(modelViewProjection, 0, projectionMatrix, 0, modelViewMatrix, 0);

                // activatrigidBodyTarget.xmle the shader program and bind the vertex/normal/tex coords
                GLES20.glUseProgram(shaderProgramID);

                GLES20.glVertexAttribPointer(vertexHandle, 3, GLES20.GL_FLOAT,
                        false, 0, mCubeObject.getVertices());
                GLES20.glUniform1f(opacityHandle, 0.3f);
                GLES20.glUniform3f(colorHandle, 0.0f, 0.0f, 0.0f);
                GLES20.glVertexAttribPointer(textureCoordHandle, 2,
                        GLES20.GL_FLOAT, false, 0, mCubeObject.getTexCoords());

                GLES20.glEnableVertexAttribArray(vertexHandle);
                GLES20.glEnableVertexAttribArray(textureCoordHandle);

                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                if(name.equals("annies_bar"))
                {
                    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures.get(0).mTextureID[0]);
                }
                if(name.equals("luna_bar"))
                {
                    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures.get(1).mTextureID[0]);
                }

//            SquareObject squareObject = new SquareObject();
//            GLES20.glVertexAttribPointer(vertexHandle, 3, GLES20.GL_FLOAT,
//                    false, 0, squareObject.getVertices());
//            GLES20.glUniform1f(opacityHandle, 0.3f);
//            GLES20.glUniform3f(colorHandle, 0.0f, 0.0f, 0.0f);
//            GLES20.glVertexAttribPointer(textureCoordHandle, 2,
//                    GLES20.GL_FLOAT, false, 0, squareObject.getTexCoords());
//
//            GLES20.glEnableVertexAttribArray(vertexHandle);
//            GLES20.glEnableVertexAttribArray(textureCoordHandle);
//
//            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,
//                    mTextures.get(0).mTextureID[0]);

                GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false,
                        modelViewProjection, 0);
                GLES20.glUniform1i(texSampler2DHandle, 0);


                // pass the model view matrix to the shader
                GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false,
                        modelViewProjection, 0);

                // finally render
                GLES20.glDrawElements(GLES20.GL_TRIANGLES,
                        mCubeObject.getNumObjectIndex(), GLES20.GL_UNSIGNED_SHORT,
                        mCubeObject.getIndices());

                // disable the enabled arrays
                GLES20.glDisableVertexAttribArray(vertexHandle);
                GLES20.glDisableVertexAttribArray(textureCoordHandle);
            }
            }
            SampleUtils.checkGLError("Render Frame");



        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_BLEND);

        mRenderer.end();
        count = (count + 1) % 50;
//        Log.d(LOGTAG, "" + count);
    }


    private void printUserData(Trackable trackable)
    {
        String userData = (String) trackable.getUserData();
        Log.d(LOGTAG, "UserData:Retreived User Data	\"" + userData + "\"");
    }


    public void setTextures(Vector<Texture> textures)
    {
        mTextures = textures;

    }

}