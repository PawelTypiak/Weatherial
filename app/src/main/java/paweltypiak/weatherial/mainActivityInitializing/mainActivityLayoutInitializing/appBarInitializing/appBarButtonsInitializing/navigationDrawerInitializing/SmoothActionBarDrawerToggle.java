package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing.navigationDrawerInitializing;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {
    //smooth drawer toggle - action is called after drawer is hide
    private Activity activity;
    private Runnable runnable;
    //private Runnable invalidateOptionsMenuRunnable;

    public SmoothActionBarDrawerToggle(Activity activity,
                                       DrawerLayout drawerLayout,
                                       Toolbar toolbar,
                                       int openDrawerContentDescRes,
                                       int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        this.activity=activity;
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        invalidateOptionsMenu();
    }

    @Override
    public void onDrawerClosed(View view) {
        super.onDrawerClosed(view);
        invalidateOptionsMenu();
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        super.onDrawerStateChanged(newState);
        if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
            runnable.run();
            runnable = null;
        }
    }

    public void runWhenIdle(Runnable runnable) {
        this.runnable = runnable;
    }

    private void invalidateOptionsMenu(){
        activity.invalidateOptionsMenu();
    }
}