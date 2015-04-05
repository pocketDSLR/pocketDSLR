package cs495.pocketdslr;

import android.content.Context;
import android.hardware.camera2.*;
import android.os.Handler;
import android.view.Surface;

import java.util.List;

/**
 * Created by Chris on 3/11/2015.
 */
public class PocketDSLRCamera implements CameraStateCallback {

    protected Context context;
    protected PocketDSLRCameraCallback cameraCallback;
    protected CameraManager cameraManager;
    protected CameraDevice cameraDevice;
    protected CameraCharacteristics cameraCharacteristics;
    protected Surface previewSurface;
    protected String cameraId;

    public PocketDSLRCamera(Context context, PocketDSLRCameraCallback cameraCallback) throws CameraAccessException {
        this.context = context;
        this.cameraCallback = cameraCallback;
        this.cameraManager = (CameraManager)this.context.getSystemService(Context.CAMERA_SERVICE);

            this.openCamera();
    }

    public Boolean isReady()
    {
        return this.cameraDevice != null;
    }

    public void requestCapture(
            Surface surface,
            ManualCameraSettings settings,
            CameraCaptureSession session,
            CameraCaptureRequestCallback callback) throws CameraAccessException {
        //add surface session to camera device

        CameraCaptureRequestCallbackBridge bridge = new CameraCaptureRequestCallbackBridge(callback);

        CaptureRequest captureRequest = this.buildCaptureRequest(surface, settings);
        session.capture(captureRequest, bridge, null);
    }

    public void onCameraStart(CameraDevice cameraDevice) throws CameraAccessException {

        this.cameraDevice = cameraDevice;
        this.cameraCallback.onCameraReady(this);
    }

    public void setupPreview(
            List<Surface> outputs,
            CameraCaptureSessionCallback callback,
            Handler handler) throws CameraAccessException {

        CameraCaptureSessionCallbackBridge bridge = new CameraCaptureSessionCallbackBridge(callback);

        if (this.isReady())
        {
            this.cameraDevice.createCaptureSession(outputs, bridge, handler);
        }
    }

    @Override
    public void onCameraOpened(CameraDevice camera) throws CameraAccessException {
        this.cameraDevice = cameraDevice;
        this.cameraCallback.onCameraReady(this);
    }

    @Override
    public void onCameraClosed(CameraDevice camera) {
        this.onCameraFinish(camera);
    }

    @Override
    public void onCameraDisconnected(CameraDevice camera) {
        this.onCameraFinish(camera);
    }

    @Override
    public void onCameraError(CameraDevice camera, int error) {
        this.onCameraFinish(camera);
    }

    public void close() {
        this.cameraDevice.close();
    }

    public CameraCharacteristics getCameraCharacteristics() {
        return this.cameraCharacteristics;
    }

    protected void openCamera() throws CameraAccessException {

        CameraStateCallbackBridge bridge = new CameraStateCallbackBridge(this);
        String cameraId = "";
        this.cameraCharacteristics = this.cameraManager.getCameraCharacteristics(cameraId);
        this.cameraManager.openCamera(cameraId, bridge, null);
    }

    protected void onCameraFinish(CameraDevice cameraDevice){

        if (this.cameraDevice == cameraDevice)
        {
            this.cameraDevice = null;
            this.cameraCallback.onCameraDestroy(this);
        }
    }

    protected CaptureRequest buildCaptureRequest(Surface renderSurface, ManualCameraSettings manualSettings) {
        return null;
    }
}
