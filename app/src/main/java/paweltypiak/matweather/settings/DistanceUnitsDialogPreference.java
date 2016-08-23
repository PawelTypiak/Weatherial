package paweltypiak.matweather.settings;

import android.content.Context;
import android.util.AttributeSet;

public class DistanceUnitsDialogPreference extends UnitsDialogPreference{

    public DistanceUnitsDialogPreference(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        setUnitId(2);
        setUnitDialogTitle();
        setPreferenceSummary();
    }
}
