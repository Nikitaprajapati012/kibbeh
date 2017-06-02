package app.kibbeh.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

public class MyFontButton extends Button {
    private static final String TAG = "TextView";
    private Typeface typeface;

    public MyFontButton(Context context) {
        super(context);
    }

    public MyFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, "");
    }

    public MyFontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, "");
    }


    private boolean setCustomFont(Context ctx, String asset) {
        try {
            if (this.typeface == null) {
                Log.i(TAG, "asset:: fonts/" + asset);
                this.typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/avenirroman.ttf");
            }
            setTypeface(this.typeface);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }
    }
}
