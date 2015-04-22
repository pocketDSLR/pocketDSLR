package cs495.pocketdslr;

import android.content.SharedPreferences;
import android.graphics.Camera;
import android.os.Bundle;
import android.util.Pair;
import android.view.Surface;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by Chris on 3/11/2015.
 */
public class ManualCameraSettings {

    public static final String SHUTTER_SPEED = "SHUTTER_SPEED";
    public static final String ISO = "ISO";
    public static final String WHITE_BALANCE = "WHITE_BALANCE";
    public static final String IMAGE_QUALITY = "IMAGE_QUALITY";
    public static final String APERTURE_SIZE = "APERTURE_SIZE";
    public static final String EXPOSURE_TIME = "EXPOSURE_TIME";

    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    protected CameraSetting isoSetting;
    protected CameraSetting exposureSetting;
    protected CameraSetting apertureSetting;

    public ManualCameraSettings(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
        this.editor = this.sharedPreferences.edit();
    }

    public void setKey(String settingKey, int value) {
        this.editor.putInt(settingKey, value);
        this.editor.commit();
    }

    public int getKey(String settingKey) {

        return this.sharedPreferences.getInt(settingKey, 0);
    }

    public Integer getISO() {

        if (this.isoSetting == null) {
            return null;
        }

        int storedValue = this.getKey(ISO);

        Pair<Integer, String> settingValue = this.isoSetting.getSettingValue(storedValue);

        return settingValue.first;
    }

    public Float getAperture() {

        if (this.apertureSetting == null) {
            return null;
        }

        float[] lookup = (float[])this.apertureSetting.lookup;

        int storedValue = this.getKey(APERTURE_SIZE);

        Pair<Integer, String> settingValue = this.apertureSetting.getSettingValue(storedValue);

        return lookup[settingValue.first];
    }

    public Long getExposureTime(){

        if (this.exposureSetting == null) {
            return null;
        }

        int storedValue = this.getKey(EXPOSURE_TIME);

        Pair<Integer, String> settingValue = this.exposureSetting.getSettingValue(storedValue);

        return (long)settingValue.first * (long)1000000;
    }

    public void registerSetting(String settingKey, CameraSetting cameraSetting) {

        switch (settingKey) {
            case ISO:
                this.isoSetting = cameraSetting;
                break;
            case EXPOSURE_TIME:
                this.exposureSetting = cameraSetting;
                break;
            case APERTURE_SIZE:
                this.apertureSetting = cameraSetting;
                break;
        }
    }
}
