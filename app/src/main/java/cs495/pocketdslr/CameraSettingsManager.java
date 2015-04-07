package cs495.pocketdslr;

import android.app.Activity;
import android.content.Context;
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

    public CameraSettingsManager(Activity activity, ManualCameraSettings manualCameraSettings) {

        this.activity = activity;
        this.initializeSettingButtons(manualCameraSettings);
    }

    protected void initializeSettingButtons(ManualCameraSettings manualCameraSettings) {

        CameraSettingButton buttonISO = (CameraSettingButton)this.activity.findViewById(R.id.buttonISO);
        CameraSettingButton buttonApertureSize = (CameraSettingButton)this.activity.findViewById(R.id.buttonApertureSize);
        CameraSettingButton buttonExposureTime = (CameraSettingButton)this.activity.findViewById(R.id.buttonExposureTime);

        ImageButton buttonLeft = (ImageButton)this.activity.findViewById(R.id.buttonChevronLeft);
        ImageButton buttonRight = (ImageButton)this.activity.findViewById(R.id.buttonChevronRight);

        this.settingSeekBar = (SeekBar)this.activity.findViewById(R.id.seekBar);

        buttonISO.registerManualCameraSettings(manualCameraSettings);
        buttonApertureSize.registerManualCameraSettings(manualCameraSettings);
        buttonExposureTime.registerManualCameraSettings(manualCameraSettings);

        buttonISO.setOnChangeListener(this);
        buttonApertureSize.setOnChangeListener(this);
        buttonExposureTime.setOnChangeListener(this);

        this.activeCameraSettingButton = buttonISO;
        this.settingSeekBar.setOnSeekBarChangeListener(this);
        this.onProgressChanged(this.settingSeekBar, 0, false);
    }

    public void onSettingChange(CameraSettingButton cameraSettingButton) {

        this.activeCameraSettingButton = cameraSettingButton;

        int settingValue = this.activeCameraSettingButton.getCameraSetting();

        this.settingSeekBar.setProgress(settingValue);
        this.settingSeekBar.setThumbOffset(settingValue);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        this.activeCameraSettingButton.setCameraSetting(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
