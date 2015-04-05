package cs495.pocketdslr;

import android.hardware.camera2.CameraAccessException;

/**
 * Created by Chris on 3/11/2015.
 */
public interface PocketDSLRCameraCallback {
    void onCameraReady(PocketDSLRCamera camera) throws CameraAccessException;
    void onCameraDestroy(PocketDSLRCamera camera);
}
