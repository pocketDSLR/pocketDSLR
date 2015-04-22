package cs495.pocketdslr;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.GradientDrawable;
import android.hardware.camera2.*;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Chris on 3/11/2015.
 */
public class PocketDSLRCamera implements
        CameraStateCallback,
        CameraCaptureSessionStateCallback,
        //TextureView.SurfaceTextureListener,
        CameraSettingChange,
        CameraCaptureSessionCallback,
        ImageReader.OnImageAvailableListener{

    protected Context context;
    protected Activity activity;
    protected CameraManager cameraManager;
    protected CameraDevice cameraDevice;
    protected CameraCharacteristics cameraCharacteristics;
    protected String cameraId;
    protected Size cameraSize;
    protected Size rawCameraSize;
    protected long minOutputFrameDuration;
    protected TextureView cameraPreview;
    protected CaptureRequest.Builder cameraCaptureRequestBuilder;
    protected CameraCaptureSession cameraCaptureSession;
    protected UserContext userContext;
    protected Handler cameraPreviewHandler;
    protected Surface previewSurface;

    public PocketDSLRCamera(Activity activity, UserContext userContext, TextureView cameraPreview) {
        this.activity = activity;
        this.context = this.activity.getBaseContext();
        this.userContext = userContext;
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

        if (this.cameraDevice == null) {
            return;
        }

        try {

            SurfaceTexture surfaceTexture = this.cameraPreview.getSurfaceTexture();

            surfaceTexture.setDefaultBufferSize(this.cameraSize.getWidth(), this.cameraSize.getHeight());

            this.previewSurface = new Surface(surfaceTexture);

            this.cameraCaptureRequestBuilder  = this.cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            this.cameraCaptureRequestBuilder.addTarget(this.previewSurface);

            this.updateSettings(this.cameraCaptureRequestBuilder);

            HandlerThread cameraPreviewThread = new HandlerThread("cameraPreview");

            cameraPreviewThread.start();

            this.cameraPreviewHandler = new Handler(cameraPreviewThread.getLooper());

            CaptureRequest captureRequest = this.cameraCaptureRequestBuilder.build();

            this.cameraCaptureSession.setRepeatingRequest(captureRequest, null, this.cameraPreviewHandler);
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

        if (this.cameraPreview == null || this.cameraSize == null || this.cameraDevice == null){
            return;
        }

        try {
            SurfaceTexture surfaceTexture = this.cameraPreview.getSurfaceTexture();

            surfaceTexture.setDefaultBufferSize(this.cameraSize.getWidth(), this.cameraSize.getHeight());

            Surface previewSurface = new Surface(surfaceTexture);

            List<Surface> previewSurfaces = new LinkedList<>();

            previewSurfaces.add(previewSurface);

            CameraCaptureSessionStateCallbackBridge cameraCaptureSessionCallbackBridge = new CameraCaptureSessionStateCallbackBridge(this);

            this.cameraDevice.createCaptureSession(previewSurfaces, cameraCaptureSessionCallbackBridge, null);
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

            this.rawCameraSize = config.getOutputSizes(ImageFormat.RAW_SENSOR)[0];

            this.minOutputFrameDuration = config.getOutputMinFrameDuration(ImageFormat.RAW_SENSOR, this.rawCameraSize);

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
        }
    }

    protected void updateSettings(CaptureRequest.Builder builder) {

        //builder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_OFF);
        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_MODE_OFF);
        builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_MODE_OFF);

        Integer isoSetting = this.userContext.getCameraSettings().getISO();

        if (isoSetting != null) {
            builder.set(CaptureRequest.SENSOR_SENSITIVITY, isoSetting);
        }

        Long exposureSetting = this.userContext.getCameraSettings().getExposureTime();

        if (exposureSetting != null) {
            builder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, exposureSetting);
        }

        Float apertureSetting = this.userContext.getCameraSettings().getAperture();

        if (apertureSetting != null) {
            builder.set(CaptureRequest.LENS_FOCUS_DISTANCE, apertureSetting);
        }

        builder.set(CaptureRequest.SENSOR_FRAME_DURATION, this.minOutputFrameDuration);
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

    public void takePicture() throws CameraAccessException {

        final ImageReader imageReader = ImageReader.newInstance(this.rawCameraSize.getWidth(), this.rawCameraSize.getHeight(), ImageFormat.RAW_SENSOR, 1);

        List<Surface> renderSurfaces = new LinkedList<Surface>();

        renderSurfaces.add(imageReader.getSurface());
        renderSurfaces.add(this.previewSurface);

        final CaptureRequest.Builder captureRequestBuilder = this.cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);

        captureRequestBuilder.set(CaptureRequest.STATISTICS_LENS_SHADING_MAP_MODE, CaptureRequest.STATISTICS_LENS_SHADING_MAP_MODE_ON);

        Range<Integer> availableFpsRanges[] = this.cameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);

        Range<Integer> targetFps = availableFpsRanges[availableFpsRanges.length - 1];

        captureRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, targetFps);

        captureRequestBuilder.addTarget(imageReader.getSurface());

        int surfaceOrientation = this.activity.getWindowManager().getDefaultDisplay().getRotation();

        switch (surfaceOrientation) {
            case Surface.ROTATION_0:
                surfaceOrientation = 90;
                break;
            case Surface.ROTATION_90:
                surfaceOrientation = 0;
                break;
            case Surface.ROTATION_180:
                surfaceOrientation = 270;
                break;
            case Surface.ROTATION_270:
                surfaceOrientation = 180;
                break;
        }

        captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, surfaceOrientation);

        this.updateSettings(captureRequestBuilder);

        HandlerThread handlerThread = new HandlerThread("TakePicture");

        handlerThread.start();

        final Handler threadHandler = new Handler(handlerThread.getLooper());

        imageReader.setOnImageAvailableListener(this, threadHandler);

        //CameraCaptureSessionStateCallbackBridge cameraCaptureSessionStateCallbackBridge = new CameraCaptureSessionStateCallbackBridge(this);

        this.cameraDevice.createCaptureSession(renderSurfaces, new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(CameraCaptureSession session) {

                try {

                    session.capture(captureRequestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                        @Override
                        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                            super.onCaptureCompleted(session, request, result);

                            resultRaw = result;


                        }
                    }, threadHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(CameraCaptureSession session) {

            }
        }, threadHandler);
    }

    TotalCaptureResult resultRaw;
    @Override
    public void onImageAvailable(ImageReader reader) {

        DngSaver dngSaver = new DngSaver(userContext, reader, cameraCharacteristics, resultRaw);

        Thread thread = new Thread(dngSaver);

        thread.start();

//        File appDirectory = new File(Environment.getExternalStorageDirectory() + "/pocketDSLR/");
//
//        if (!appDirectory.exists()){
//            appDirectory.mkdirs();
//        }
//
//        SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_");
//
//        Date currentDate = new Date();
//
//        Random randomGenerator = new Random();
//
//        final String imageFileName = appDirectory.toString() + simpleDateFormatter.format(currentDate) + randomGenerator.nextInt(1000) + ".dng";
//
//        File imageFile = new File(imageFileName);
//
//        FileOutputStream imageFileStream = null;
//        try {
//            imageFileStream = new FileOutputStream(imageFile);
//            imageFileStream.write((byte)1);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        DngCreator dngImageCreator = new DngCreator(cameraCharacteristics, resultRaw);
//
//        try {
//            dngImageCreator.writeImage(imageFileStream, reader.acquireNextImage());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            imageFileStream.flush();
//            imageFileStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        dngImageCreator.close();

        this.setupCameraPreview();
    }

    @Override
    public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {

    }


    @Override
    public void onSettingchange() {

        if (this.cameraCaptureSession == null){
            return;
        }
        try {
            this.updateSettings(this.cameraCaptureRequestBuilder);
            this.cameraCaptureSession.setRepeatingRequest(this.cameraCaptureRequestBuilder.build(), null, this.cameraPreviewHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
