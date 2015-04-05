package cs495.pocketdslr;

import android.hardware.camera2.*;

import java.io.IOException;

/**
 * Created by Chris on 3/11/2015.
 */
public interface CameraCaptureRequestCallback {
    void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) throws IOException;
    void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure);
    void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult);
    void onCaptureSequenceAborted(CameraCaptureSession session, int sequenceId);
    void onCaptureSequenceCompleted(CameraCaptureSession session, int sequenceId, long frameNumber);
    void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber);
}
