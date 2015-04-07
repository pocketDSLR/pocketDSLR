package cs495.pocketdslr;

import android.hardware.camera2.CameraCaptureSession;

/**
 * Created by Chris on 3/11/2015.
 */
public class CameraCaptureSessionStateCallbackBridge extends CameraCaptureSession.StateCallback {

    private CameraCaptureSessionStateCallback callback;

    public CameraCaptureSessionStateCallbackBridge(CameraCaptureSessionStateCallback callback)
    {
        this.callback = callback;
    }

    @Override
    public void onConfigured(CameraCaptureSession session) {

        this.callback.onConfigured(session);
    }

    @Override
    public void onConfigureFailed(CameraCaptureSession session) {

        this.callback.onConfigureFailed(session);
    }
}
