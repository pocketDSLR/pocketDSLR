package cs495.pocketdslr;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.hardware.camera2.CameraAccessException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


public class CameraActivity extends Activity {

    TextureView cameraPreview;
    PocketDSLRContext pocketDSLRContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_camera);

        this.cameraPreview = (TextureView)this.findViewById(R.id.cameraPreview);
        this.pocketDSLRContext = new PocketDSLRContext(this, savedInstanceState, this.cameraPreview);
    }

    protected void onPause() {
        super.onPause();
        this.pocketDSLRContext.getCamera().close();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        if (newConfig.orientation != Configuration.ORIENTATION_PORTRAIT) {
            super.onConfigurationChanged(newConfig);
        }
    }

}
