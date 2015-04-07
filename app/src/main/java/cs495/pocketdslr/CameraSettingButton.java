package cs495.pocketdslr;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

/**
 * Created by Chris on 3/11/2015.
 */
public class CameraSettingButton extends RotatableButton implements View.OnClickListener {

    protected CameraSetting cameraSetting;
    protected CameraSettingListener cameraSettingListener;
    protected ManualCameraSettings manualCameraSettings;

    public CameraSettingButton(Context context) {
        super(context);
    }

    public CameraSettingButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.pocketDSLR);

        String settingKey = styledAttributes.getString(R.styleable.pocketDSLR_setting);

        this.initializeCameraSetting(settingKey);
    }

    public CameraSettingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.pocketDSLR, defStyleAttr, 0);

        String settingKey = styledAttributes.getString(R.styleable.pocketDSLR_setting);

        this.initializeCameraSetting(settingKey);
    }

    private void initializeCameraSetting(String settingKey) {

        switch (settingKey) {
            case ManualCameraSettings.EXPOSURE_TIME:
                this.cameraSetting = CameraSetting.createExposeTimeCameraSetting();
                break;
            case ManualCameraSettings.ISO:
                this.cameraSetting = CameraSetting.createISOCameraSetting();
                break;
            case ManualCameraSettings.APERTURE_SIZE:
                this.cameraSetting = CameraSetting.createApertureCameraSetting();
                break;
        }
    }

    public void registerManualCameraSettings(ManualCameraSettings manualCameraSettings) {

        this.manualCameraSettings = manualCameraSettings;

        int storedValue = this.manualCameraSettings.getKey(this.cameraSetting.getSettingKey());

        Pair<Integer, String> settingValue = this.cameraSetting.getSettingValue(storedValue);

        this.setText(settingValue.second);
    }

    public void setOnChangeListener(CameraSettingListener cameraSettingListener) {

        this.cameraSettingListener = cameraSettingListener;
        this.setOnClickListener(this);
    }

    public void setCameraSetting(int value) {

        Pair<Integer, String> settingValue = this.cameraSetting.translateSettingValue(value);

        this.setText(settingValue.second);

        this.manualCameraSettings.setKey(this.cameraSetting.getSettingKey(), settingValue.first);
    }

    public int getCameraSetting() {

        int settingValue = this.manualCameraSettings.getKey(this.cameraSetting.getSettingKey());

        return this.cameraSetting.reverseTranslatedValue(settingValue);
    }

    @Override
    public void onClick(View v) {
        if (this.cameraSettingListener != null){
            this.cameraSettingListener.onSettingChange(this);
        }
    }
}
