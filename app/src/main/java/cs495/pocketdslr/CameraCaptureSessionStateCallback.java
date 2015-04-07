package cs495.pocketdslr;

import android.hardware.camera2.CameraCaptureSession;

/**
 * Created by Chris on 3/11/2015.
 */
public interface CameraCaptureSessionStateCallback {
    abstract void onConfigureFailed(CameraCaptureSession session);
    abstract void onConfigured(CameraCaptureSession session);
}
