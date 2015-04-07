package cs495.pocketdslr;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;

/**
 * Created by Chris on 4/7/2015.
 */
public interface CameraCaptureSessionCallback {

    void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result);
}
