package cs495.pocketdslr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.DngCreator;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Chris on 3/11/2015.
 */
public final class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, CameraCaptureRequestCallback{

    protected PocketDSLRContext pocketDSLRContext;

    public CameraPreview(Context context, PocketDSLRContext pocketDSLRContext) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean result = super.onTouchEvent(event);

        CameraCaptureSessionCallback sessionCallback;

        int action = event.getAction();

        try {
            this.handleCameraCapture();
        } catch (CameraAccessException e) {
        } catch (IOException e) {
        }

        return result;
    }

    protected void handleCameraCapture() throws CameraAccessException, IOException {

        ImageReader imageReader = null;

        Surface surface = imageReader.getSurface();

        CameraCaptureSession captureSession = null;

        ManualCameraSettings settings = this.pocketDSLRContext.getUser().getCameraSettings();

        this.pocketDSLRContext.getCamera().requestCapture(surface, settings, captureSession, this);

        boolean captureSuccess = true;

        CaptureRequest captureRequest = null;
        TotalCaptureResult captureResult = null;
        CaptureFailure failure = null;

        if (captureSuccess){
            this.onCaptureCompleted(captureSession, captureRequest, captureResult);
        }
        else {
            this.onCaptureFailed(captureSession, captureRequest, failure);
        }
    }

    protected void onViewImage(String name) {

        Activity cameraActivity = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(cameraActivity);
// Add the buttons
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();

        boolean viewImageInGallery = true;

        if (viewImageInGallery) {
            this.pocketDSLRContext.getUser().viewImage(name);
        }
    }

    @Override
    public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) throws IOException {
        CameraCharacteristics cameraCharacteristics = this.pocketDSLRContext.getCamera().getCameraCharacteristics();
        DngCreator dngCreator = new DngCreator(cameraCharacteristics, result);
        OutputStream imageStream = null;
        Image image = null;
        dngCreator.writeImage(imageStream, image);
        String imageName = "";
        this.pocketDSLRContext.getUser().saveImage(image, imageName);
    }

    @Override
    public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure) {

    }

    @Override
    public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {

    }

    @Override
    public void onCaptureSequenceAborted(CameraCaptureSession session, int sequenceId) {

    }

    @Override
    public void onCaptureSequenceCompleted(CameraCaptureSession session, int sequenceId, long frameNumber) {

    }

    @Override
    public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {

    }
}
