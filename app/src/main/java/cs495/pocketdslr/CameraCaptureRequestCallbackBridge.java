package cs495.pocketdslr;

import android.hardware.camera2.*;

/**
 * Created by Chris on 3/11/2015.
 */
public class CameraCaptureRequestCallbackBridge extends CameraCaptureSession.CaptureCallback {

    protected CameraCaptureRequestCallback callback;

    public CameraCaptureRequestCallbackBridge(CameraCaptureRequestCallback callback){
        this.callback = callback;
    }

    public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result){

    }

    public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure){

    }

    public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult){

    }

    public void onCaptureSequenceAborted(CameraCaptureSession session, int sequenceId){

    }

    public void onCaptureSequenceCompleted(CameraCaptureSession session, int sequenceId, long frameNumber){

    }

    public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber){

    }
}
