package paweltypiak.matweather.layoutInitializing.appBarInitializing;

import android.app.Activity;
import android.widget.TextView;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import paweltypiak.matweather.R;
import paweltypiak.matweather.layoutInitializing.appBarInitializing.appBarButtonsInitializing.AppBarLayoutButtonsInitializer;
import paweltypiak.matweather.layoutInitializing.appBarInitializing.appBarOnOffsetChangeListenerAdding.ToolbarTitleClickableViewSizeUpdater;
import paweltypiak.matweather.usefulClasses.FavouritesEditor;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataFormatter;

import static paweltypiak.matweather.usefulClasses.UsefulFunctions.getFormattedString;
import static paweltypiak.matweather.usefulClasses.UsefulFunctions.setViewGone;
import static paweltypiak.matweather.usefulClasses.UsefulFunctions.setViewVisible;

public class AppBarLayoutDataInitializer {

    private AppBarLayoutButtonsInitializer buttonsInitializer;
    private TextView timeTextView;
    private TextView timezoneTextView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView subtitle;

    public AppBarLayoutDataInitializer(Activity activity, AppBarLayoutButtonsInitializer appBarLayoutButtonsInitializer){
        this.buttonsInitializer=appBarLayoutButtonsInitializer;
        findViews(activity);
    }

    private void findViews(Activity activity){
        timeTextView=(TextView)activity.findViewById(R.id.toolbar_layout_time_layout_current_time_text);
        timezoneTextView =(TextView)activity.findViewById(R.id.toolbar_layout_time_layout_timezone_text);
        collapsingToolbarLayout=(CollapsingToolbarLayout)activity.findViewById(R.id.collapsing_toolbar_layout);
        subtitle=(TextView)activity.findViewById(R.id.toolbar_layout_subtitle_text);
    }

    public void updateAppBarLayoutData(Activity activity, WeatherDataFormatter weatherDataFormatter, boolean isGeolocalizationMode){
        if(FavouritesEditor.isAddressEqual(activity)){
            FavouritesEditor.setLayoutForFavourites(activity);
        }
        else{
            String city=weatherDataFormatter.getCity();
            String region=weatherDataFormatter.getRegion();
            String country=weatherDataFormatter.getCountry();
            UsefulFunctions.setAppBarStrings(activity,city,region+", "+country);
            buttonsInitializer.
                    getFloatingActionButtonInitializer().
                    setFloatingActionButtonOnClickIndicator(0);
            if(isGeolocalizationMode==true){
                buttonsInitializer
                        .getNavigationDrawerInitializer()
                        .checkNavigationDrawerMenuItem(0);
            }
            else {
                buttonsInitializer
                        .getNavigationDrawerInitializer()
                        .uncheckAllNavigationDrawerMenuItems();
            }
        }
        UsefulFunctions.setViewGone(timezoneTextView);
        timezoneTextView.setText(weatherDataFormatter.getTimezone());
        UsefulFunctions.setViewVisible(timezoneTextView);
    }

    public void updateTimeTextView(CharSequence time){
        timeTextView.setText(time);
    }

    public String[] getAppBarLocationName(){
        //get location name from AppBar
        String[] location=new String[2];
        location[0]=collapsingToolbarLayout.getTitle().toString();
        location[1]=subtitle.getText().toString();
        return location;
    }

    public void setAppBarLocationName(Activity activity, String toolbarTitle, String toolbarSubtitle){
        //set custom location name in AppBar
        String formattedToolbarTitle=getFormattedString(toolbarTitle);
        collapsingToolbarLayout.setTitle(formattedToolbarTitle);
        subtitle.setText(toolbarSubtitle);
        setViewGone(subtitle);
        if(!toolbarSubtitle.equals("")) setViewVisible(subtitle);
        ToolbarTitleClickableViewSizeUpdater.getOnAppBarStringsChangeListener().onAppBarStringsChanged(activity,formattedToolbarTitle);
    }
}
