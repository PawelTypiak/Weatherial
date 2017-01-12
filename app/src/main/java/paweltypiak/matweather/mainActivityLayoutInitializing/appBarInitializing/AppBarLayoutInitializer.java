package paweltypiak.matweather.mainActivityLayoutInitializing.appBarInitializing;

import android.app.Activity;

import paweltypiak.matweather.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing.AppBarLayoutButtonsInitializer;
import paweltypiak.matweather.mainActivityLayoutInitializing.appBarInitializing.appBarOnOffsetChangeListenerAdding.AppBarOnOffsetChangeListenerAdder;
import paweltypiak.matweather.usefulClasses.DialogInitializer;

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
        appBarLayoutDataInitializer=new AppBarLayoutDataInitializer(activity,this);
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
