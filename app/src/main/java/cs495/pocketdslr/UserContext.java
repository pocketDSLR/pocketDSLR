package cs495.pocketdslr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by Chris on 3/11/2015.
 */
public class UserContext {

    protected Context context;
    protected ManualCameraSettings cameraSettings;

    public UserContext(Activity activity) {

        this.context = activity;
        this.cameraSettings = new ManualCameraSettings(activity.getPreferences(Activity.MODE_PRIVATE));
    }

    public ManualCameraSettings getCameraSettings() {

        return this.cameraSettings;
    }

    public void saveImage(Image image, String name) {

    }

    public void viewImage(String name) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file:///mnt/sdcard/pocketDSLR/imgs/" + name),"image/*");
        this.context.startActivity(intent);
    }

}
