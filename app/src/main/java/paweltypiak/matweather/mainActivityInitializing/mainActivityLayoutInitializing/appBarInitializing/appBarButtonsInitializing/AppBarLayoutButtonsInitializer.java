package paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing;

import android.app.Activity;

import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing.navigationDrawerInitializing.NavigationDrawerInitializer;

public class AppBarLayoutButtonsInitializer {

    private NavigationDrawerInitializer navigationDrawerInitializer;
    private FloatingActionButtonInitializer floatingActionButtonInitializer;

    public AppBarLayoutButtonsInitializer(Activity activity){
        initializeNavigationDrawer(activity);
        initializeSearchButton(activity);
        initializeToolbarTitleButton(activity);
        initializeYahooLogoButton(activity);
        initializeFloatingActionButtonOnClickListener(activity);
    }

    private void initializeNavigationDrawer(Activity activity){
        navigationDrawerInitializer=new NavigationDrawerInitializer(
                activity);
    }

    private void initializeSearchButton(Activity activity){
        new SearchButtonInitializer(
                activity);
    }

    private void initializeToolbarTitleButton(Activity activity){
        new ToolbarTitleButtonInitializer(
                activity);
    }

    private void initializeYahooLogoButton(Activity activity){
        new YahooLogoButtonInitializer(activity);
    }

    private void initializeFloatingActionButtonOnClickListener(Activity activity){
        floatingActionButtonInitializer=new FloatingActionButtonInitializer(activity);
    }

    public NavigationDrawerInitializer getNavigationDrawerInitializer() {
        return navigationDrawerInitializer;
    }

    public FloatingActionButtonInitializer getFloatingActionButtonInitializer() {
        return floatingActionButtonInitializer;
    }
}
