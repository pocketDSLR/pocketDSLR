package cs495.pocketdslr;

import android.hardware.camera2.CameraCaptureSession;

/**
 * Created by Chris on 3/11/2015.
 */
public interface CameraCaptureSessionCallback{
    void onActive(CameraCaptureSession session);
    void onClosed(CameraCaptureSession session);
    abstract void onConfigureFailed(CameraCaptureSession session);
    abstract void onConfigured(CameraCaptureSession session);
    void onReady(CameraCaptureSession session);
}
