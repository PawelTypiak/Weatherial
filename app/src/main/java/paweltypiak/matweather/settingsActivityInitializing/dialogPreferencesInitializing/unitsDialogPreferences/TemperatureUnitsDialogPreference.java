package paweltypiak.matweather.settingsActivityInitializing.dialogPreferencesInitializing.unitsDialogPreferences;


import android.content.Context;
import android.util.AttributeSet;

public class TemperatureUnitsDialogPreference extends UnitsDialogPreference{

    public TemperatureUnitsDialogPreference(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        setUnitId(0);
        setUnitDialogTitle();
        setPreferenceSummary();
    }
}
