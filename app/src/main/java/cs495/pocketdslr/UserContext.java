package cs495.pocketdslr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.DngCreator;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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

    public void saveImage(CameraCharacteristics cameraCharacteristics, TotalCaptureResult totalCaptureResult, ImageReader imageReader) {

        File appDirectory = new File(Environment.getExternalStorageDirectory()+ "/pocketDSLR/");

        if (!appDirectory.exists()){
            appDirectory.mkdir();
        }

        SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_");

        Date currentDate = new Date();

        Random randomGenerator = new Random();

        final String imageFileName = simpleDateFormatter.format(currentDate) + randomGenerator.nextInt(1000) + ".dng";

        File imageFile = new File(appDirectory, imageFileName);
        Log.v("pocketDSLR!!~~", imageFile.toString());
        try {
            FileOutputStream imageFileStream = new FileOutputStream(imageFile);

            DngCreator dngImageCreator = new DngCreator(cameraCharacteristics, totalCaptureResult);

            dngImageCreator.writeImage(imageFileStream, imageReader.acquireNextImage());

            AlertDialog.Builder previewPromptDialog = new AlertDialog.Builder(context);

            previewPromptDialog.setMessage("View Image?");

            previewPromptDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    viewImage(imageFileName);
                }
            });

            previewPromptDialog.setNegativeButton("No", null);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void viewImage(String fileLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(fileLocation), "image/*");
        this.context.startActivity(intent);
    }
}
