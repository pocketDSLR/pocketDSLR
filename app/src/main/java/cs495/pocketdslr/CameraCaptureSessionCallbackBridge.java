package cs495.pocketdslr;

import android.hardware.camera2.CameraCaptureSession;

/**
 * Created by Chris on 3/11/2015.
 */
public class CameraCaptureSessionCallbackBridge extends CameraCaptureSession.StateCallback {

    private CameraCaptureSessionCallback callback;

    public CameraCaptureSessionCallbackBridge(CameraCaptureSessionCallback callback)
    {
        this.callback = callback;
    }

    @Override
    public void onReady(CameraCaptureSession session) {
        super.onReady(session);
        this.callback.onReady(session);
    }

    @Override
    public void onActive(CameraCaptureSession session) {
        super.onActive(session);
        this.callback.onActive(session);
    }

    @Override
    public void onClosed(CameraCaptureSession session) {
        super.onClosed(session);
        this.callback.onClosed(session);
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
