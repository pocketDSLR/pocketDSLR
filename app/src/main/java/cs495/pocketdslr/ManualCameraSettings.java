package cs495.pocketdslr;

import android.os.Bundle;
import android.view.Surface;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by Chris on 3/11/2015.
 */
public class ManualCameraSettings {

    public static final String SHUTTER_SPEED = "1";
    public static final String ISO = "2";
    public static final String WHITE_BALANCE = "3";
    public static final String IMAGE_QUALITY = "4";
    public static final String APERATURE_SIZE = "5";

    public final String[] SETTING_KEYS = new String[5];

    protected int shutterSpeed;
    protected int iso;
    protected int whiteBalance;
    protected int imageQuality;
    protected int aperatureSize;

    protected Bundle savedInstance;

    public ManualCameraSettings(Bundle savedInstance){

    }

    public void setShutterSpeed(int shutterSpeed) {
        this.savedInstance.putInt(SHUTTER_SPEED, shutterSpeed);
    }

    public int getShutterSpeed() {
        int setting = this.savedInstance.getInt(SHUTTER_SPEED);
        return setting;
    }

    public void setISO(int iso) {
        this.savedInstance.putInt(ISO, iso);
    }

    public int getISO() {
        int setting =  this.savedInstance.getInt(ISO);
        return setting;
    }

    public void setAperature(int aperatureSize) {
        this.savedInstance.putInt(APERATURE_SIZE, aperatureSize);
    }

    public int getAperature() {
        int setting = this.savedInstance.getInt(APERATURE_SIZE);
        return setting;
    }

    public void setWhiteBalance(int whiteBalance) {
        this.savedInstance.putInt(WHITE_BALANCE, whiteBalance);
    }

    public int getWhiteBalance(){
        int setting = this.savedInstance.getInt(WHITE_BALANCE);
        return setting;
    }

    public void setImageQuality(int imageQuality) {
        this.savedInstance.putInt(IMAGE_QUALITY, imageQuality);
    }

    public int getImageQuality(){
        int setting = this.savedInstance.getInt(IMAGE_QUALITY);
        return setting;
    }

    public Boolean hasCapabilities() {
        return false;
    }

    public void requestCapture(Surface renderSurface) {

    }

}
