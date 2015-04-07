package cs495.pocketdslr;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

/**
 * Created by Chris on 4/6/2015.
 */

public class RotatableButton extends Button {

    private Integer orientation;

    public RotatableButton(Context context) {
        super(context);

        this.orientation = 0;
    }

    public RotatableButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.pocketDSLR);

        String orientationAttribute = styledAttributes.getString(R.styleable.pocketDSLR_orientation);

        this.orientation = Integer.parseInt(orientationAttribute);

        Log.println(1, "Orientation", orientationAttribute);
    }

    public RotatableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.pocketDSLR, defStyleAttr, 0);

        String orientationAttribute = styledAttributes.getString(R.styleable.pocketDSLR_orientation);

        this.orientation = Integer.parseInt(orientationAttribute);

        Log.println(1, "Orientation", orientationAttribute);
    }

    @Override
    protected void onDraw(Canvas c) {

        c.rotate(this.orientation, getWidth() / 2, getHeight() / 2);
        super.onDraw(c);
    }
}
