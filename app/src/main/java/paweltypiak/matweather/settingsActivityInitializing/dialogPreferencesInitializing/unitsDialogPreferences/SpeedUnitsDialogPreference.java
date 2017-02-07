package paweltypiak.matweather.settingsActivityInitializing.dialogPreferencesInitializing.unitsDialogPreferences;

import android.content.Context;
import android.util.AttributeSet;

public class SpeedUnitsDialogPreference extends UnitsDialogPreference{

    public SpeedUnitsDialogPreference(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        setUnitId(1);
        setUnitDialogTitle();
        setPreferenceSummary();
    }
}
