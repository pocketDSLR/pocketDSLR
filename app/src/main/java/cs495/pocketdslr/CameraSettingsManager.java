package cs495.pocketdslr;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.util.Range;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Chris on 3/11/2015.
 */
public class CameraSettingsManager implements CameraSettingListener, SeekBar.OnSeekBarChangeListener
{
    protected SeekBar settingSeekBar;
    protected Activity activity;
    protected CameraSettingButton activeCameraSettingButton;
    protected PocketDSLRCamera pocketDSLRCamera;
    protected ManualCameraSettings manualCameraSettings;

    public CameraSettingsManager(Activity activity, ManualCameraSettings manualCameraSettings, PocketDSLRCamera pocketDSLRCamera) {

        this.activity = activity;
        this.pocketDSLRCamera = pocketDSLRCamera;
        this.manualCameraSettings = manualCameraSettings;
    }

    public void initializeSettingButtons() {

        CameraCharacteristics cameraCharacteristics = this.pocketDSLRCamera.getCameraCharacteristics();

        CameraSettingButton buttonISO = (CameraSettingButton)this.activity.findViewById(R.id.buttonISO);
        CameraSettingButton buttonApertureSize = (CameraSettingButton)this.activity.findViewById(R.id.buttonApertureSize);
        CameraSettingButton buttonExposureTime = (CameraSettingButton)this.activity.findViewById(R.id.buttonExposureTime);

        this.settingSeekBar = (SeekBar)this.activity.findViewById(R.id.seekBar);

        //Register the manual camera settings with the buttons so they can initialize to stored
        //values and also save further changes to the app state
        buttonISO.registerManualCameraSettings(this.manualCameraSettings, cameraCharacteristics);
        buttonApertureSize.registerManualCameraSettings(this.manualCameraSettings, cameraCharacteristics);
        buttonExposureTime.registerManualCameraSettings(this.manualCameraSettings, cameraCharacteristics);

        //Setup button listeners for when a setting button has been selected, effectively
        //changing which setting is being adjusted and controlled by the seekbar
        buttonISO.setOnChangeListener(this);
        buttonApertureSize.setOnChangeListener(this);
        buttonExposureTime.setOnChangeListener(this);

        //Setup default setting button. The ISO camera setting button will be selected by default
        this.activeCameraSettingButton = buttonISO;
        this.settingSeekBar.setOnSeekBarChangeListener(this);
        this.onProgressChanged(this.settingSeekBar, 0, false);

        //Setup detailed seekbar movement with the left and right chevron buttons.
        ImageButton buttonLeft = (ImageButton)this.activity.findViewById(R.id.buttonChevronLeft);
        ImageButton buttonRight = (ImageButton)this.activity.findViewById(R.id.buttonChevronRight);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int progress = settingSeekBar.getProgress();

                if (progress > 0) {
                    settingSeekBar.setProgress(progress - 1);
                }
            }
        });
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int progress = settingSeekBar.getProgress();

                if (progress < 100) {
                    settingSeekBar.setProgress(progress + 1);
                }
            }
        });

        ImageButton takePictureButton = (ImageButton)this.activity.findViewById(R.id.buttonCamera);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pocketDSLRCamera.takePicture();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //Occurs when a setting button it selected.
    //Setups up the seekbar with the correct value for that setting
    public void onSettingChange(CameraSettingButton cameraSettingButton) {

        this.activeCameraSettingButton = cameraSettingButton;

        int settingValue = this.activeCameraSettingButton.getCameraSetting();

        this.settingSeekBar.setProgress(settingValue);
    }

    //Occurs when the seekbar is change.
    //This changes the setting corresponding to the currently active setting button.
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        this.activeCameraSettingButton.setCameraSetting(progress);
        this.pocketDSLRCamera.onSettingchange();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
