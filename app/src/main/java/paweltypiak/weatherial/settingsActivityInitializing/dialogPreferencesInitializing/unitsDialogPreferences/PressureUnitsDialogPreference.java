package paweltypiak.weatherial.settingsActivityInitializing.dialogPreferencesInitializing.unitsDialogPreferences;

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
