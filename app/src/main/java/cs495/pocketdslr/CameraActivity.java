package cs495.pocketdslr;

import android.app.Activity;
import android.hardware.camera2.CameraAccessException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;


public class CameraActivity extends Activity {

    protected PocketDSLRContext pocketDSLRContext;
    protected CameraSettingsView settingsView;
    protected CameraPreview cameraPreview;

    public CameraActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        SurfaceView surfaceView = null;
        try {
            this.pocketDSLRContext = new PocketDSLRContext(this, savedInstanceState, surfaceView);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        this.settingsView = new CameraSettingsView(this,this.pocketDSLRContext.getUser());
        this.cameraPreview = new CameraPreview(this, this.pocketDSLRContext);
    }

    @Override
    protected  void onStart() {

    }

    @Override
    protected void onResume() {

    }

    @Override
    protected  void onPause() {

    }

    @Override
    protected  void onStop() {

    }

    @Override
    protected void onDestroy() {

    }
}
