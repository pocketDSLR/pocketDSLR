package cs495.pocketdslr;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;

/**
 * Created by Chris on 3/11/2015.
 */
public class CameraStateCallbackBridge extends CameraDevice.StateCallback {

    protected CameraStateCallback callback;

    public CameraStateCallbackBridge(CameraStateCallback callback)
    {
        this.callback = callback;
    }

    public void onOpened(CameraDevice camera) {
        try {
            callback.onCameraOpened(camera);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClosed(CameraDevice camera)
    {
        super.onClosed(camera);
        callback.onCameraClosed(camera);
    }

    public void onDisconnected(CameraDevice camera)
    {
        callback.onCameraDisconnected(camera);
    }

    public void onError(CameraDevice camera, int error){
        callback.onCameraError(camera, error);
    }

}
