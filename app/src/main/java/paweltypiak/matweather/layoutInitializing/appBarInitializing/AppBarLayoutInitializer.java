package paweltypiak.matweather.layoutInitializing.appBarInitializing;

import android.app.Activity;

import paweltypiak.matweather.layoutInitializing.appBarInitializing.appBarButtonsInitializing.AppBarLayoutButtonsInitializer;
import paweltypiak.matweather.layoutInitializing.appBarInitializing.appBarOnOffsetChangeListenerAdding.AppBarOnOffsetChangeListenerAdder;
import paweltypiak.matweather.usefulClasses.DialogInitializer;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataFormatter;

public class AppBarLayoutInitializer {

    private AppBarLayoutDimensionsSetter appBarLayoutDimensionsSetter;
    private AppBarLayoutButtonsInitializer appBarLayoutButtonsInitializer;
    private AppBarOnOffsetChangeListenerAdder appBarOnOffsetChangeListenerAdder;
    private AppBarLayoutDataInitializer appBarLayoutDataInitializer;


    public AppBarLayoutInitializer(Activity activity, DialogInitializer dialogInitializer){
        setAppBarDimensions(activity);
        initializeAppBarButtons(activity,dialogInitializer);
        addAppBarOnOffsetChangeListener(activity);
        initializeAppBarLayoutData(activity);
    }

    private void setAppBarDimensions(Activity activity){
        appBarLayoutDimensionsSetter=new AppBarLayoutDimensionsSetter(activity);
    }

    private void initializeAppBarButtons(Activity activity,DialogInitializer dialogInitializer){
        appBarLayoutButtonsInitializer=new AppBarLayoutButtonsInitializer(activity,dialogInitializer);
    }

    private void addAppBarOnOffsetChangeListener(Activity activity){
        appBarOnOffsetChangeListenerAdder=new AppBarOnOffsetChangeListenerAdder(activity);
    }

    private void initializeAppBarLayoutData(Activity activity){
        appBarLayoutDataInitializer=new AppBarLayoutDataInitializer(activity,appBarLayoutButtonsInitializer);
    }

    public AppBarLayoutDimensionsSetter getAppBarLayoutDimensionsSetter() {
        return appBarLayoutDimensionsSetter;
    }

    public AppBarLayoutButtonsInitializer getAppBarLayoutButtonsInitializer() {
        return appBarLayoutButtonsInitializer;
    }

    public AppBarOnOffsetChangeListenerAdder getAppBarOnOffsetChangeListenerAdder() {
        return appBarOnOffsetChangeListenerAdder;
    }

    public AppBarLayoutDataInitializer getAppBarLayoutDataInitializer() {
        return appBarLayoutDataInitializer;
    }
}
