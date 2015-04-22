package cs495.pocketdslr;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.media.ImageReader;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Chris on 3/11/2015.
 */
public class PocketDSLRContext implements PocketDSLRCameraCallback, TextureView.SurfaceTextureListener {

    private PocketDSLRCamera camera;
    private CameraSettingsManager cameraSettingsManager;
    private UserContext user;

    public PocketDSLRContext(Activity activity, TextureView cameraPreview) {

        this.user = new UserContext(activity);
        this.camera = new PocketDSLRCamera(activity, this.user, cameraPreview);
        this.cameraSettingsManager = new CameraSettingsManager(activity, this.user.getCameraSettings(), camera);

        cameraPreview.setSurfaceTextureListener(this);
    }

    public PocketDSLRCamera getCamera() {

        return this.camera;
    }

    public UserContext getUser() {

        return this.user;
    }

    protected void onReadyState() {
        this.camera.onReadyState();
        this.cameraSettingsManager.initializeSettingButtons();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        this.onReadyState();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        this.onReadyState();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onCameraReady(PocketDSLRCamera camera) throws CameraAccessException {

    }

    @Override
    public void onCameraDestroy(PocketDSLRCamera camera) {

    }
}
