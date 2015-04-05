package cs495.pocketdslr;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;

/**
 * Created by Chris on 3/11/2015.
 */
public interface CameraStateCallback{
    void onCameraOpened(CameraDevice camera) throws CameraAccessException;
    void onCameraClosed(CameraDevice camera);
    void onCameraDisconnected(CameraDevice camera);
    void onCameraError(CameraDevice camera, int error);
}
