package paweltypiak.matweather.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing;

import android.app.Activity;

import paweltypiak.matweather.usefulClasses.DialogInitializer;

public class AppBarLayoutButtonsInitializer {

    private NavigationDrawerInitializer navigationDrawerInitializer;
    private FloatingActionButtonInitializer floatingActionButtonInitializer;

    public AppBarLayoutButtonsInitializer(Activity activity,
                                          DialogInitializer dialogInitializer){
        initializeNavigationDrawer(activity, dialogInitializer);
        initializeSearchButton(activity, dialogInitializer);
        initializeToolbarTitleButton(activity, dialogInitializer);
        initializeYahooLogoButton(activity, dialogInitializer);
        initializeFloatingActionButtonOnClickListener(activity, dialogInitializer);
    }

    private void initializeNavigationDrawer(Activity activity,
                                            DialogInitializer dialogInitializer){
        navigationDrawerInitializer=new NavigationDrawerInitializer(
                activity,
                dialogInitializer);
    }

    private void initializeSearchButton(Activity activity,
                                        DialogInitializer dialogInitializer){
        new SearchButtonInitializer(
                activity,
                dialogInitializer);
    }

    private void initializeToolbarTitleButton(Activity activity,
                                              DialogInitializer dialogInitializer){
        new ToolbarTitleButtonInitializer(
                activity,
                dialogInitializer);
    }

    private void initializeYahooLogoButton(Activity activity,
                                           DialogInitializer dialogInitializer){
        new YahooLogoButtonInitializer(activity,dialogInitializer);
    }

    private void initializeFloatingActionButtonOnClickListener(Activity activity,
                                                               DialogInitializer dialogInitializer){
        floatingActionButtonInitializer=new FloatingActionButtonInitializer(activity,dialogInitializer);
    }

    public NavigationDrawerInitializer getNavigationDrawerInitializer() {
        return navigationDrawerInitializer;
    }

    public FloatingActionButtonInitializer getFloatingActionButtonInitializer() {
        return floatingActionButtonInitializer;
    }
}
