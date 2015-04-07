package cs495.pocketdslr;

import android.app.Activity;
import android.content.Context;
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
public class PocketDSLRContext implements PocketDSLRCameraCallback {

    private PocketDSLRCamera camera;
    private CameraSettingsManager cameraSettingsManager;
    private UserContext user;

    public PocketDSLRContext(Activity activity, TextureView cameraPreview) {

        this.user = new UserContext(activity);
        this.camera = new PocketDSLRCamera(activity, this.user, cameraPreview);
        this.cameraSettingsManager = new CameraSettingsManager(activity, this.user.getCameraSettings());

        cameraPreview.setSurfaceTextureListener(this.camera);
    }

    public PocketDSLRCamera getCamera() {

        return this.camera;
    }

    public UserContext getUser() {

        return this.user;
    }

    public void onReadyState() {
        this.camera.onReadyState();
    }

    @Override
    public void onCameraReady(PocketDSLRCamera camera) throws CameraAccessException {

    }

    @Override
    public void onCameraDestroy(PocketDSLRCamera camera) {

    }
}
