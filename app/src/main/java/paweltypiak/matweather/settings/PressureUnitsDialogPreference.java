package paweltypiak.matweather.settings;

import android.content.Context;
import android.util.AttributeSet;

public class PressureUnitsDialogPreference extends UnitsDialogPreference {

    public PressureUnitsDialogPreference(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        setUnitId(2);
        setUnitDialogTitle();
        setPreferenceSummary();
    }
}
