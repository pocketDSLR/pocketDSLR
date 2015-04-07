package cs495.pocketdslr;

import android.content.SharedPreferences;
import android.os.Bundle;
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

    public void setShutterSpeed(int shutterSpeed) {
        this.setKey(SHUTTER_SPEED, shutterSpeed);
    }

    public int getShutterSpeed() {
        return this.getKey(SHUTTER_SPEED);
    }

    public void setISO(int iso) {
        this.setKey(ISO, iso);
    }

    public int getISO() {
        return this.getKey(ISO);
    }

    public void setAperture(int apertureSize) {
        this.setKey(APERTURE_SIZE, apertureSize);
    }

    public int getAperture() {
        return this.getKey(APERTURE_SIZE);
    }

    public void setWhiteBalance(int whiteBalance) {
        this.setKey(WHITE_BALANCE, whiteBalance);
    }

    public int getWhiteBalance(){
        return this.getKey(WHITE_BALANCE);
    }

    public void setImageQuality(int imageQuality) {
        this.setKey(IMAGE_QUALITY, imageQuality);
    }

    public int getImageQuality(){
        return this.getKey(IMAGE_QUALITY);
    }

    public void setExposureTime(int exposureTime){
        this.setKey(EXPOSURE_TIME, exposureTime);
    }

    public int getExposureTime(){
        return this.getKey(EXPOSURE_TIME);
    }

}
