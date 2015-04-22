package cs495.pocketdslr;

import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.TotalCaptureResult;
import android.media.ImageReader;

/**
 * Created by Chris on 4/22/2015.
 */
public class DngSaver implements Runnable {

    protected CameraCharacteristics cameraCharacteristics;
    protected ImageReader imageReader;
    protected TotalCaptureResult totalCaptureResult;
    protected UserContext userContext;

    public DngSaver(UserContext userContext, ImageReader imageReader, CameraCharacteristics cameraCharacteristics, TotalCaptureResult totalCaptureResult){

        this.userContext = userContext;
        this.imageReader = imageReader;
        this.cameraCharacteristics = cameraCharacteristics;
        this.totalCaptureResult = totalCaptureResult;
    }

    @Override
    public void run() {
        this.userContext.saveImage(this.cameraCharacteristics, this.totalCaptureResult, this.imageReader);

        this.imageReader.close();
    }
}
