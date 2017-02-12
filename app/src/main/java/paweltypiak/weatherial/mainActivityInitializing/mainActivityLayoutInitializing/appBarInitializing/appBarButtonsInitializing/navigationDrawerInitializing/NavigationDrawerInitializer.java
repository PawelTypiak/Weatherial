/*******************************************************************************
 * Copyright 2017 Paweł Typiak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing.navigationDrawerInitializing;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import paweltypiak.weatherial.R;

public class NavigationDrawerInitializer {

    private SmoothActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    public NavigationDrawerInitializer(Activity activity){
        initializeToolbar(activity);
        initializeActionBarDrawerToggle(activity);
        initializeNavigationView(activity);
    }

    private void initializeToolbar(Activity activity){
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar_layout_collapsed_toolbar);
        ((AppCompatActivity)activity).setSupportActionBar(toolbar);
    }

    private void initializeActionBarDrawerToggle(Activity activity){
        drawerLayout = (DrawerLayout)  activity.findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new SmoothActionBarDrawerToggle(
                activity,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void initializeNavigationView(Activity activity){
        navigationView = (NavigationView)  activity.findViewById(R.id.navigation_view);
        initializeHeader(activity);
        setItemSelectedListener(activity);
    }

    private void initializeHeader(Activity activity){
        new NavigationDrawerHeaderInitializer(activity,navigationView);
    }

    private void setItemSelectedListener(Activity activity){
        new NavigationItemSelectedListenerSetter(
                activity,
                actionBarDrawerToggle,
                drawerLayout,
                navigationView);
    }

    public void uncheckAllNavigationDrawerMenuItems(){
        //uncheck all items in navigation drawer
        MenuItem geolocalizationItem=navigationView.getMenu().findItem(R.id.nav_button_geolocalization);
        MenuItem favouritesItem=navigationView.getMenu().findItem(R.id.nav_button_favourites);
        geolocalizationItem.setChecked(false);
        geolocalizationItem.setCheckable(false);
        favouritesItem.setChecked(false);
        favouritesItem.setCheckable(false);
    }

    public void uncheckNavigationDrawerMenuItem(int itemId){
        MenuItem geolocalizationItem=navigationView.getMenu().findItem(R.id.nav_button_geolocalization);
        MenuItem favouritesItem=navigationView.getMenu().findItem(R.id.nav_button_favourites);
        if(itemId==0) {
            geolocalizationItem.setChecked(false);
        }
        else if(itemId==1) {
            favouritesItem.setChecked(false);
        }
    }

    public void checkNavigationDrawerMenuItem(int itemId){
        MenuItem geolocalizationItem=navigationView.getMenu().findItem(R.id.nav_button_geolocalization);
        MenuItem favouritesItem=navigationView.getMenu().findItem(R.id.nav_button_favourites);
        if(itemId==0) {
            geolocalizationItem.setCheckable(true);
            geolocalizationItem.setChecked(true);
            favouritesItem.setCheckable(false);
            favouritesItem.setChecked(false);

        }
        else if(itemId==1) {
            favouritesItem.setCheckable(true);
            favouritesItem.setChecked(true);
            geolocalizationItem.setCheckable(false);
            geolocalizationItem.setChecked(false);
        }
    }
    public void openDrawerLayout(){
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public boolean closeDrawerLayout(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else {
            return false;
        }
    }
}

