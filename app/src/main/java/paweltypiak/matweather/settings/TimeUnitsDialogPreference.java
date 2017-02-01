package paweltypiak.matweather.settings;

import android.content.Context;
import android.util.AttributeSet;

public class TimeUnitsDialogPreference extends UnitsDialogPreference{

    public TimeUnitsDialogPreference(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        setUnitId(3);
        setUnitDialogTitle();
        setPreferenceSummary();
    }
}
