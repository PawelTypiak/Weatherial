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
package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing;

import android.app.Activity;

import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing.navigationDrawerInitializing.NavigationDrawerInitializer;

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
