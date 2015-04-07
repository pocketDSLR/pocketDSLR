package cs495.pocketdslr;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.GradientDrawable;
import android.hardware.camera2.*;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Chris on 3/11/2015.
 */
public class PocketDSLRCamera implements
        CameraStateCallback,
        CameraCaptureSessionStateCallback,
        TextureView.SurfaceTextureListener,
        CameraCaptureSessionCallback {

    protected Context context;
    protected Activity activity;
    protected CameraManager cameraManager;
    protected CameraDevice cameraDevice;
    protected CameraCharacteristics cameraCharacteristics;
    protected String cameraId;
    protected Size cameraSize;
    protected TextureView cameraPreview;
    protected CaptureRequest.Builder cameraCaptureRequestBuilder;
    protected CameraCaptureSession cameraCaptureSession;
    protected ImageReader.OnImageAvailableListener imageAvailableListener;

    public PocketDSLRCamera(Activity activity, ImageReader.OnImageAvailableListener imageAvailableListener, TextureView cameraPreview) {
        this.activity = activity;
        this.context = this.activity.getBaseContext();
        this.imageAvailableListener = imageAvailableListener;
        this.cameraManager = (CameraManager)this.context.getSystemService(Context.CAMERA_SERVICE);
        this.cameraPreview = cameraPreview;
    }

    @Override
    public void onConfigureFailed(CameraCaptureSession session) {
        Toast.makeText(this.context, "Camera capture session failed", Toast.LENGTH_SHORT);
    }

    @Override
    public void onConfigured(CameraCaptureSession session) {

        this.cameraCaptureSession = session;

        if (this.cameraDevice == null || this.cameraCaptureRequestBuilder == null) {
            return;
        }

        try {

            this.cameraCaptureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);

            HandlerThread cameraPreviewThread = new HandlerThread("cameraPreview");

            cameraPreviewThread.start();

            Handler cameraPreviewHandler = new Handler(cameraPreviewThread.getLooper());

            CaptureRequest captureRequest = this.cameraCaptureRequestBuilder.build();

            this.cameraCaptureSession.setRepeatingRequest(captureRequest, null, cameraPreviewHandler);
        }
        catch(CameraAccessException e) {
            Log.w("pocketDSLR", e.getMessage(), e);
        }
    }

    //Setup preview
    @Override
    public void onCameraOpened(CameraDevice cameraDevice) {

        this.cameraDevice = cameraDevice;

        setupCameraPreview();
    }

    @Override
    public void onCameraClosed(CameraDevice camera) {

        this.onCameraFinish(camera);
    }

    @Override
    public void onCameraDisconnected(CameraDevice camera) {

        this.onCameraFinish(camera);
    }

    @Override
    public void onCameraError(CameraDevice camera, int error) {

        this.onCameraFinish(camera);
    }

    public void close() {
        if (this.cameraDevice != null) {
            this.cameraDevice.close();
            this.cameraDevice = null;
        }
    }

    public CameraCharacteristics getCameraCharacteristics() {

        return this.cameraCharacteristics;
    }

    public void onReadyState() {

        this.openCamera();
    }

    protected void setupCameraPreview() {

        try {
//            Matrix transform = this.cameraPreview.getTransform(null);
//
//            transform.setRotate(90);
//
//            this.cameraPreview.setTransform(transform);

            SurfaceTexture surfaceTexture = this.cameraPreview.getSurfaceTexture();

            surfaceTexture.setDefaultBufferSize(this.cameraSize.getWidth(), this.cameraSize.getHeight());

            Surface previewSurface = new Surface(surfaceTexture);

            this.cameraCaptureRequestBuilder = this.cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            this.cameraCaptureRequestBuilder.addTarget(previewSurface);

            List<Surface> previewSurfaces = new LinkedList<>();
            previewSurfaces.add(previewSurface);

            CameraCaptureSessionStateCallbackBridge cameraCaptureSessionCallbackBridge = new CameraCaptureSessionStateCallbackBridge(this);

            this.cameraDevice.createCaptureSession(previewSurfaces, cameraCaptureSessionCallbackBridge, null);

            //this.cameraCallback.onCameraReady(this);
        }
        catch (CameraAccessException e) {

            Log.println(0, "pocketDSLR", e.getMessage());
        }
    }

    protected void openCamera() {

        try {

            this.cameraId = this.cameraManager.getCameraIdList()[0];

            this.cameraCharacteristics = this.cameraManager.getCameraCharacteristics(this.cameraId);

            StreamConfigurationMap config = this.cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            this.cameraSize = config.getOutputSizes(SurfaceTexture.class)[0];

            //this.applyTransform(this.cameraSize.getWidth(), this.cameraSize.getHeight());



            CameraStateCallbackBridge cameraStateCallbackBridge = new CameraStateCallbackBridge(this);

            this.cameraManager.openCamera(this.cameraId, cameraStateCallbackBridge, null);
        }
        catch (CameraAccessException ex) {
            Log.println(0, "pocketDSLR", ex.getMessage());
            Toast.makeText(this.context, "Unable to acquire camera", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onCameraFinish(CameraDevice cameraDevice){

        if (this.cameraDevice == cameraDevice)
        {
            this.cameraDevice = null;
            //this.cameraCallback.onCameraDestroy(this);
        }
    }

    protected CaptureRequest buildCaptureRequest(Surface renderSurface, ManualCameraSettings manualSettings) {
        return null;
    }

    private void applyTransform() {

        if (this.cameraPreview == null || this.cameraSize == null) {
            return;
        }

        RectF viewRect = new RectF(0, 0, this.cameraPreview.getWidth(), this.cameraPreview.getHeight());
        RectF bufferRect = new RectF(0, 0, this.cameraSize.getWidth(), this.cameraSize.getHeight());

        bufferRect.offset(viewRect.centerX() - bufferRect.centerX(), viewRect.centerY() - bufferRect.centerY());

        Matrix transformMatrix = new Matrix();

        transformMatrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.CENTER);

        int surfaceOrientation = this.activity.getWindowManager().getDefaultDisplay().getRotation();

        transformMatrix.postRotate(surfaceOrientation * 90, viewRect.centerX(), viewRect.centerY());

        this.cameraPreview.setTransform(transformMatrix);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        this.openCamera();
        //this.applyTransform();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        this.openCamera();
        //this.applyTransform();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    //TODO
    public void takePicture() throws CameraAccessException {

        ImageReader imageReader = ImageReader.newInstance(this.cameraSize.getWidth(), this.cameraSize.getHeight(), ImageFormat.JPEG, 1);

        List<Surface> renderSurfaces = new LinkedList<Surface>();

        renderSurfaces.add(imageReader.getSurface());
        renderSurfaces.add(new Surface(this.cameraPreview.getSurfaceTexture()));

        CaptureRequest.Builder captureRequestBuilder = this.cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);

        captureRequestBuilder.addTarget(imageReader.getSurface());

        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);

        int surfaceOrientation = this.activity.getWindowManager().getDefaultDisplay().getRotation();

        captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, surfaceOrientation);

        HandlerThread handlerThread = new HandlerThread("TakePicture");

        handlerThread.start();

        Handler threadHandler = new Handler(handlerThread.getLooper());

        imageReader.setOnImageAvailableListener(this.imageAvailableListener, threadHandler);

        CameraCaptureSessionCallbackBridge cameraCaptureSessionCallbackBridge = new CameraCaptureSessionCallbackBridge(this);

        //this.cameraDevice.createCaptureSession(renderSurfaces,
    }

    @Override
    public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {

    }
}
