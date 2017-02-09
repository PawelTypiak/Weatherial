package paweltypiak.weatherial.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class RobotoMediumFontTextView extends TextView {
    public RobotoMediumFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Medium.ttf"));
    }
}
