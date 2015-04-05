package cs495.pocketdslr;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.SeekBar;

import java.util.List;

/**
 * Created by Chris on 3/11/2015.
 */
public class CameraSettingsView extends ViewGroup {

    protected List<CameraSettingButton> settingButtons;
    protected SeekBar currentSettingSeekBar;
    protected UserContext userContext;
    protected Context context;

    public CameraSettingsView(Context context, UserContext userContext) {
        super(context);
        this.initializeSettingButtons();
    }

    protected void initializeSettingButtons() {
        List<String> settings = null;

        for (String setting : settings){
            this.settingButtons.add(new CameraSettingButton(this.context, setting));
        }
    }

    protected void onSelectedSettingChange(CameraSettingButton button) {
        this.currentSettingSeekBar = button.getSettingSeekBar();
    }

    protected void onSettingChange(CameraSettingButton button) {

        String settingKey = button.getSettingKey();
        int settingValue = (int)button.getSettingValue();

        switch (settingKey)
        {
            case ManualCameraSettings.APERATURE_SIZE:
                this.userContext.getCameraSettings().setAperature(settingValue);
            case ManualCameraSettings.IMAGE_QUALITY:
                this.userContext.getCameraSettings().setImageQuality(settingValue);
            case ManualCameraSettings.ISO:
                this.userContext.getCameraSettings().setISO(settingValue);
            case ManualCameraSettings.SHUTTER_SPEED:
                this.userContext.getCameraSettings().setShutterSpeed(settingValue);
            case ManualCameraSettings.WHITE_BALANCE:
                this.userContext.getCameraSettings().setWhiteBalance(settingValue);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.initializeSettingButtons();
    }
}
