package paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing;

import android.app.Activity;

import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing.AppBarLayoutButtonsInitializer;
import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarOnOffsetChangeListenerAdding.AppBarOnOffsetChangeListenerAdder;

public class AppBarLayoutInitializer {

    private AppBarLayoutDimensionsSetter appBarLayoutDimensionsSetter;
    private AppBarLayoutButtonsInitializer appBarLayoutButtonsInitializer;
    private AppBarOnOffsetChangeListenerAdder appBarOnOffsetChangeListenerAdder;
    private AppBarLayoutDataInitializer appBarLayoutDataInitializer;


    public AppBarLayoutInitializer(Activity activity){
        setAppBarDimensions(activity);
        initializeAppBarButtons(activity);
        addAppBarOnOffsetChangeListener(activity);
        initializeAppBarLayoutData(activity);
    }

    private void setAppBarDimensions(Activity activity){
        appBarLayoutDimensionsSetter=new AppBarLayoutDimensionsSetter(activity);
    }

    private void initializeAppBarButtons(Activity activity){
        appBarLayoutButtonsInitializer=new AppBarLayoutButtonsInitializer(activity);
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
