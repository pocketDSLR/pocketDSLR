package cs495.pocketdslr;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;

/**
 * Created by Chris on 4/7/2015.
 */
public class CameraCaptureSessionCallbackBridge extends CameraCaptureSession.CaptureCallback {

    private CameraCaptureSessionCallback cameraCaptureSessionCallback;

    public CameraCaptureSessionCallbackBridge(CameraCaptureSessionCallback cameraCaptureSessionCallback) {

        this.cameraCaptureSessionCallback = cameraCaptureSessionCallback;
    }

    @Override
    public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
        super.onCaptureCompleted(session, request, result);

        this.cameraCaptureSessionCallback.onCaptureCompleted(session, request, result);
    }
}
