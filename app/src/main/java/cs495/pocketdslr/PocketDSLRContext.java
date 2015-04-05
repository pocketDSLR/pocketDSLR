package cs495.pocketdslr;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.media.ImageReader;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Chris on 3/11/2015.
 */
public class PocketDSLRContext implements PocketDSLRCameraCallback, CameraCaptureSessionCallback {

    private PocketDSLRCamera camera;
    private UserContext user;
    private Context context;
    private SurfaceView surfaceView;

    public PocketDSLRContext(Context context, Bundle bundle, SurfaceView surfaceView) throws CameraAccessException {
        this.context = context;
        this.camera = new PocketDSLRCamera(context, this);
        this.user = new UserContext(context, bundle);
    }

    public PocketDSLRCamera getCamera() { return this.camera; }
    public UserContext getUser() { return this.user; }

    public void onCameraReady(PocketDSLRCamera camera) throws CameraAccessException {

        List<Surface> surfaces = new LinkedList<Surface>();
        surfaces.add(this.surfaceView.getHolder().getSurface());

        this.camera.setupPreview(surfaces, this, null);
    }

    public void onCameraDestroy(PocketDSLRCamera camera){

    }

    @Override
    public void onActive(CameraCaptureSession session) {

    }

    @Override
    public void onClosed(CameraCaptureSession session) {

    }

    @Override
    public void onConfigureFailed(CameraCaptureSession session) {

    }

    @Override
    public void onConfigured(CameraCaptureSession session) {

    }

    @Override
    public void onReady(CameraCaptureSession session) {

    }
}
