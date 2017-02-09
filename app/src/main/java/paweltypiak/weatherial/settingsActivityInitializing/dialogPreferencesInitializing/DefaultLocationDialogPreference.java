package paweltypiak.weatherial.settingsActivityInitializing.dialogPreferencesInitializing;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.util.List;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.usefulClasses.FavouritesEditor;
import paweltypiak.weatherial.usefulClasses.SharedPreferencesModifier;
import paweltypiak.weatherial.usefulClasses.UsefulFunctions;

public class DefaultLocationDialogPreference extends CustomDialogPreference {

    public DefaultLocationDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setDialogTitle(getContext().getString(R.string.preferences_default_location_title));
        setPreferenceSummary();
    }

    protected void buildRadioGroup(RadioGroup radioGroup){
        //radioGroup with favourite locations list
        final List<String> favouritesList = FavouritesEditor.getFavouriteLocationsNamesDialogList(getContext());
        String locationName;
        int size=favouritesList.size()+1;
        for(int i=0;i<size;i++){
            int radioButtonId=i;
            int radioButtonBottomMargin;
            if(i!=size-1){
                radioButtonBottomMargin=(int)getContext().getResources().getDimension(R.dimen.radio_button_bottom_margin);
                locationName=favouritesList.get(i);
            }
            else{
                radioButtonBottomMargin=0;
                locationName=getContext().getString(R.string.favourites_dialog_geolocalization_option);

            }
            RadioButton radioButton=setRadioButtonLayout(locationName,radioButtonId,radioButtonBottomMargin);
            int checkedRadioButtonId=FavouritesEditor.getDefaultLocationId(getContext());
            if(i==checkedRadioButtonId) {
                radioButton.setChecked(true);
            }
            else if(i==size-1&&checkedRadioButtonId==-1) radioButton.setChecked(true);
            radioGroup.addView(radioButton);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                FavouritesEditor.setSelectedFavouriteLocationID(i);
            }
        });
    };

    protected void onPositiveResult(){
        //this method is called when user clicks positive button
        String defeaultLocationAddress;
        int selectedLocationID=FavouritesEditor.getSelectedFavouriteLocationID();
        int numberOfFavourites=FavouritesEditor.getNumberOfFavourites(getContext());
        if(selectedLocationID==numberOfFavourites) {
            SharedPreferencesModifier.setDefaultLocationGeolocalization(getContext());
            setSummary(getContext().getString(R.string.favourites_dialog_geolocalization_option));
        }
        else {
            defeaultLocationAddress=FavouritesEditor.getSelectedFavouriteLocationAddress(getContext());
            SharedPreferencesModifier.setDefaultLocationConstant(getContext(),defeaultLocationAddress);
            setSummary(UsefulFunctions.fromHtml(FavouritesEditor.getSelectedFavouriteLocationEditedName()));
        }
        Log.d("changed_preference",getTitle()+ " preference changed to: "+getSummary());
    }

    protected void setPreferenceSummary(){
        if(SharedPreferencesModifier.isDefaultLocationConstant(getContext())){
            String defeaultLocationEditedName=FavouritesEditor.getDefaultLocationEditedName(getContext());
            setSummary(UsefulFunctions.fromHtml(defeaultLocationEditedName));
        }
        else{
            setSummary(getContext().getString(R.string.favourites_dialog_geolocalization_option));
        }
    }
}
