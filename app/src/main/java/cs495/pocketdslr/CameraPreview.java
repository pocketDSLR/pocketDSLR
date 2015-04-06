package cs495.pocketdslr;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * Created by Chris on 3/11/2015.
 */
public class CameraPreview extends TextureView {

    private int widthRatio;
    private int heightRatio;

    public CameraPreview(Context context) {
        super(context, null);

        this.initialize();
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        this.initialize();
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.initialize();
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        this.initialize();
    }

    private void initialize() {
        this.widthRatio = 0;
        this.heightRatio = 0;
    }

    public void adjustAspectRatio(int width, int height) {
        this.heightRatio = height;
        this.widthRatio = width;
        super.requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (this.widthRatio == 0 || this.heightRatio == 0) {
            //super.setMeasuredDimension(widthSize, heightSize);
            super.setMeasuredDimension(1440, 1080);
        }
        else if (widthSize < (double)heightSize * this.widthRatio / (double)this.heightRatio) {
            //super.setMeasuredDimension(widthSize, widthSize * this.heightRatio / this.widthRatio);
            super.setMeasuredDimension(1440, 1080);
            //super.setMeasuredDimension(widthSize * this.heightRatio / this.widthRatio, widthSize);
        }
        else {
            //super.setMeasuredDimension(heightSize * this.widthRatio / this.heightRatio, heightSize);
            super.setMeasuredDimension(1440, 1080);
        }
    }
}