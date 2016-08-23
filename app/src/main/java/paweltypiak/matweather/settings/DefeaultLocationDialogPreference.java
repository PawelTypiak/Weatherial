package paweltypiak.matweather.settings;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.util.List;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.FavouritesEditor;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;

public class DefeaultLocationDialogPreference extends CustomDialogPreference {

    public DefeaultLocationDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setDialogTitle(getContext().getString(R.string.preferences_defeault_location_title));
        setPreferenceSummary();
    }

    protected void buildRadioGroup(RadioGroup radioGroup){
        final List<String> favouritesList = FavouritesEditor.getFavouriteLocationsNamesDialogList(getContext());
        String locationName;
        int size=favouritesList.size()+1;
        for(int i=0;i<size;i++){
            int radioButtonId=i;
            int radioButtonBottomMargin;
            if(i!=size-1){
                radioButtonBottomMargin=16;
                locationName=favouritesList.get(i);
            }
            else{
                radioButtonBottomMargin=0;
                locationName=getContext().getString(R.string.favourites_dialog_geolocalization_option);

            }
            RadioButton radioButton=setRadioButtonLayout(locationName,radioButtonId,radioButtonBottomMargin);
            int checkedRadioButtonId=FavouritesEditor.getDefeaultLocationId(getContext());
            Log.d("checked radio button id", "buildRadioGroup: "+checkedRadioButtonId);
            if(i==checkedRadioButtonId) {
                radioButton.setChecked(true);
            }
            else if(i==size-1&&checkedRadioButtonId==-1) radioButton.setChecked(true);
            radioGroup.addView(radioButton);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                FavouritesEditor.setChoosenFavouriteLocationID(i);
            }
        });
    };

    protected void onPositiveResult(){
        String defeaultLocationAddress;
        int choosenLocationID=FavouritesEditor.getChoosenFavouriteLocationID();
        int numberOfFavourites=FavouritesEditor.getNumberOfFavourites(getContext());
        if(choosenLocationID==numberOfFavourites) {
            SharedPreferencesModifier.setDefeaultLocationGeolocalization(getContext());
            setSummary(getContext().getString(R.string.favourites_dialog_geolocalization_option));
        }
        else {
            defeaultLocationAddress=FavouritesEditor.getChoosenFavouriteLocationAddress(getContext());
            SharedPreferencesModifier.setDefeaultLocationConstant(getContext(),defeaultLocationAddress);
            setSummary(Html.fromHtml(FavouritesEditor.getChoosenFavouriteLocationEditedName()));
        }
        Log.d("change preference",getTitle()+ " preference changed to: "+getSummary());
    }
    protected void setPreferenceSummary(){
        if(SharedPreferencesModifier.isDefeaultLocationConstant(getContext())){
            String defeaultLocationEditedName=FavouritesEditor.getDefeaultLocationEditedName(getContext());
            setSummary(Html.fromHtml(defeaultLocationEditedName));
        }
        else{
            setSummary(getContext().getString(R.string.favourites_dialog_geolocalization_option));
        }
    }
}
