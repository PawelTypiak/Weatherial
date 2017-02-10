package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;
import paweltypiak.weatherial.R;

import static paweltypiak.weatherial.usefulClasses.UsefulFunctions.getFormattedString;

public class AppBarLayoutDataInitializer {

    private Activity activity;
    private AppBarLayoutInitializer appBarLayoutInitializer;
    private TextView currentTimeTextView;
    private TextView timezoneTextView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView subtitleTextView;

    public AppBarLayoutDataInitializer(Activity activity, AppBarLayoutInitializer appBarLayoutInitializer){
        this.activity=activity;
        this.appBarLayoutInitializer=appBarLayoutInitializer;
        findViews(activity);
    }

    private void findViews(Activity activity){
        currentTimeTextView =(TextView)activity.findViewById(R.id.toolbar_layout_time_layout_current_time_text);
        timezoneTextView =(TextView)activity.findViewById(R.id.toolbar_layout_time_layout_timezone_text);
        collapsingToolbarLayout=(CollapsingToolbarLayout)activity.findViewById(R.id.collapsing_toolbar_layout);
        subtitleTextView =(TextView)activity.findViewById(R.id.toolbar_layout_subtitle_text);
    }

    public void updateLocationName(String title, String subtitle){
        String formattedToolbarTitle=getFormattedString(title);
        collapsingToolbarLayout.setTitle(formattedToolbarTitle);
        String formattedToolbarSubitle=getFormattedString(subtitle);
        subtitleTextView.setText(formattedToolbarSubitle);
        appBarLayoutInitializer.getAppBarOnOffsetChangeListenerAdder().getToolbarTitleClickableViewSizeUpdater().
                getOnAppBarStringsChangeListener().
                onAppBarStringsChanged(activity,formattedToolbarTitle);
    }

    public void updateTimezone(String timezone){
        timezoneTextView.setVisibility(View.GONE);
        timezoneTextView.setText(timezone);
        timezoneTextView.setVisibility(View.VISIBLE);
    }

    public void updateCurrentTime(CharSequence time){
        currentTimeTextView.setText(time);
    }

    public String[] getAppBarLocationName(){
        //get location name from AppBar
        String[] location=new String[2];
        location[0]=collapsingToolbarLayout.getTitle().toString();
        location[1]= subtitleTextView.getText().toString();
        return location;
    }

    public interface OnAppBarStringsChangeListener {
        void onAppBarStringsChanged(Activity activity,String toolbarTitle);
    }
}
