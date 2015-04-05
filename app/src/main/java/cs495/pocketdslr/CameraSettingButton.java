package cs495.pocketdslr;

import android.content.Context;
import android.widget.Button;
import android.widget.SeekBar;

/**
 * Created by Chris on 3/11/2015.
 */
public class CameraSettingButton<T> extends Button {

    protected String settingKey;
    protected T value;

    public CameraSettingButton(Context context, String settingKey) {
        super(context);
    }

    public String getSettingKey(){
        return this.settingKey;
    }

    public T getSettingValue() {
        return this.value;
    }

    public SeekBar getSettingSeekBar() {
        return null;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
