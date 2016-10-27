# MatWeather
<p align="center">
<img src="/images/matweather_logo_background.png" /> 
</p>

**MatWeather** is a simple weather app for Android with a flat interface design inspired by _Material Design_. Application is dedicated to devices with at least Android 4.3 Jelly Bean. APK file is placed in [/apk](/apk) folder.

<p align="center">
<img src="images/screenshots/preview_start.png" width="250"/> <img src="images/screenshots/preview_geolocalization.png" width="250"/> <img src="images/screenshots/preview_about.png" width="250"/> 
</p>

<p align="center">
<img src="images/screenshots/preview_day.png" width="250"/> <img src="images/screenshots/preview_night.png" width="250"/> <img src="images/screenshots/preview_navigation_drawer.png" width="250"/>  
</p>

<p align="center">
<img src="images//screenshots/preview_search.png" width="250"/> <img src="images/screenshots/preview_favourites.png" width="250"/> <img src="images//screenshots/preview_settings.png" width="250"/> 
</p>

All screenshots are stored in [images/screenshots ](/images/screenshots).

### Main features
- providing weather information by **Yahoo Weather API** 
- providing address information by **Google Maps API**
- webservices response in **JSON**
- interface of application is based on **_Material Design_** guidelines provided by _Google_.
- **two language versions** - polish and english; strings are stored in different xml files
- using **AsyncTask** to download information in background
- using **interfaces** to send callbacks between classes
- creating **different UI thread** to **dynamically updade layout** with 1 sec interval
- using **Runnable** objects to pass methods between different objects
- using **fragments** with nested fragments inside them with **two way communication** between fragments and Activity
- using **Bundle** to pass data between Activities and using **Parcelable** to pass custom objects
- handling **application lifecycle** - using lifecycle methods, saving current data by using **onSavedState** method
- using **abstract classes and inheritance**
- using universal, **multi-use static classes** and methods
- possibility of **saving user's favourite locations**
- using **SharedPreferences** class for saving user preferences
- requesting **Marshmallow permissions** at Run Time
- providing drawables for **5 different densities**
- **responsiveness of layout** achieved by using weights
- providing **custom themes in Styles.xml** for activities and views
- **dynamic theme change** based on current day time
- using **dialogs with custom styles** and layouts
- using **Picasso library** to load images efficiently and change icons color programically
- using **AppCompat library** to provide Material Design views on pre-Lollipop devices
- handling **exceptions** 
- using **String class and Calendar class** for data formatting
- **no deprecations** - using proper methods dedicated to different SDK versions

### Project Description

### Launch Activity.
Layout of this activity has two nested layouts: one is for _CardView_ button on the bottom, the other one is a _FrameLayout_ for the fragments. At the beginning of the _onCreate()_ method of this activity there is loaded a fragment, depending on the information if it is the first launch of application. This information is stored in _SharedPreferences_.
####  1. First Launch
- First fragment, wchich is shown after first launch of application contains a splash screen with application logo. At the bottom of the screen there is a button, which loads the next fragment.
    <p align="center">
  <img src="images/screenshots/intro_begin.png" width="250" />
  </p>
- Another fragment contains two layouts - one is an _ImageView_ with application logo, another one is a nested fragment, which shows the configuration information.
  - Language fragment gives user the possibility to change the language version. After selecting different option the proper preference is saved to _SharedPreferences_ and the proper value for _Locale_ class is set. Then the layout is immediately reloaded to make the changes visible in a real time. 
 <p align="center">
<img src="images/screenshots/intro_language_1.png" width="250"/> <img src="images/screenshots/intro_language_2.png" width="250"/> 
 </p>
 
  - Units fragment lets the user change the measurement system. After selecting particular unit, all units' preferences are converted to a single _String_ value and saved to _SharedPreferences_.
  <p align="center">
<img src="images/screenshots/intro_units.png" width="250"/>
 </p>
 
  - Location fragment is used to choose the defeault location for which the weather information will be downloaded. 
    - First option means that the defeault location is the user's current position.
  <p align="center">
<img src="images/screenshots/intro_location.png" width="250"/>
 </p> 
    - After choosing _Different location_ option there is shown a dialog with _EditText_, where the user have to enter location. Even if the input contains some empty characters, after changing focus to the background of the dialog it is formatted to the _String_ without empty spaces and its first letter is uppercase. If the user will dismiss the dialog without entering location, after selecting _Continue_ button there is shown a dialog with the information. After choosing _OK_ button previous dialog pops up.
  <p align="center">
<img src="images/screenshots/intro_different_location_dialog_empty.png" width="250"/> <img src="images/screenshots/intro_different_location_dialog_input.png" width="250"/> 
</p>
<p align="center">
<img src="images/screenshots/intro_different_location_dialog_edited.png" width="250"/> <img src="images/screenshots/intro_different_location_full.png" width="250"/> 
 </p>
   - If the user selected _Current location_ there is loaded the geolocalization method fragment, where he can choose between using GPS or network. 
   <p align="center">
<img src="images/screenshots/intro_geolocalization_method.png" width="250"/>
 </p> 
    Then, another fragment is loaded and geolocalization process is started. If the API version is 23 or higher, the user has to give permissions to use localization. If the permission is denied, the proper dialog is shown. 
    <p align="center">
<img src="images/screenshots/intro_permissions_request.png" width="250"/> <img src="images/screenshots/intro_permissions_denied.png" width="250"/> 
 </p>
    In other case the process is continued. In this step user's current coordinates are downloaded.
    <p align="center">
<img src="images/screenshots/intro_loading_geolocalization.png" width="250"/>
 </p> 
    If the provider selected by user is unavailable, he is informed about this situation by the proper dialog. Then, if the other provider is available, it is used for geolocalization, in other case the information about geolocalization failure is shown. Next, the user can move back to the location fragment and select different location.
    <p align="center">
    <img src="images/screenshots/intro_failure_network_unavailable.png" width="250"/> <img src="images/screenshots/intro_failure_gps_unavailable.png" width="250"/> <img src="images/screenshots/intro_failure_geolocalization.png" width="250"/> 
</p>
  Received coordinates are used in _Google Maps API_ to determine the address of the current location. 
    <p align="center">
<img src="images/screenshots/intro_loading_retrieving_address.png" width="250"/>
 </p> 
 In case of internet connection loss or empty response from the service proper dialog is shown, and the user may leave the application or change the location.
 <p align="center">
<img src="images/screenshots/intro_failure_internet_connection.png" width="250"/> <img src="images/screenshots/intro_failure_geolocalization.png" width="250"/> 
 </p>
   Address data received from _Google Maps API_ is used to download weather information from _Yahoo Weather API_.
   <p align="center">
<img src="images/screenshots/intro_loading_weather_information.png" width="250"/>
 </p> 
   If there is a problem with internet connection or with the service response, information dialog is shown.
    <p align="center">
<img src="images/screenshots/intro_failure_internet_connection.png" width="250"/> <img src="images/screenshots/intro_failure_no_weather.png" width="250"/> 
 </p>
    <p align="center">
    
  - When _Different location_ is selected, weather downloading for entered location is started. 
  <p align="center">
<img src="images/screenshots/intro_loading_retrieving_location.png" width="250"/>
 </p> 
  If the response from the server is empty, it probably means that there is no weather information for this location - in this situation dialog pops up and the user has the possibility to move back to the fragment where he can change the location. User is also informed if the internet connection is interrupt, so after reconnecting the downloading process can be refreshed.  
  <p align="center">
 <img src="images/screenshots/intro_failure_internet_connection.png" width="250"/> <img src="images/screenshots/intro_failure_no_weather.png" width="250"/> 
 </p>
 When response from the server contains data, there is shown a dialog with detailed information about the search result. User can accept this location, or he can move back to the location fragment to change his choice. He can also exit from the application.
 <p align="center">
<img src="images/screenshots/intro_weather_result.png" width="250"/>
 </p> 
 - If weather data downloading process is succeed, information about defeault loation and succesfull first application launch is stored in _SharedPreferences_. After this process the object with necessary weather data is passed to the main activity by using _Parcelable_ class.
 <p align="center">
<img src="images/screenshots/intro_loading_content.png" width="250"/>
 </p> 
 
#### 2. Next launches
With every next launch starts from the fragment where all the necessary information are downloaded. Depending on the user's choice there are two types of processes that may be started - geolocalization process in case of selecting user's current location or weather information downloading in the other case. This procedure looks almost the same as it was described in previous chapter. Although, there are some small changes:

- In geolocalization mode when there is a problem with receiving current location the dialog with user's favourites is shown. This solution gives the possibility to change the location.
<p align="center">
<img src="images/screenshots/intro_favourites_list_after_failure.png" width="250"/>
 </p> 

- When the user want to download weather for the location which is different than the current one, the main activity is loaded without the information about the searched location. The reason for using this solution is because of the fact, that the user knows what is his defeault location, so the information about founding it is unnecessary.
 
### Main Activity.
In _onCreate()_ method of the main activity every weather data information passed from previous activity is loaded. 

#### 1. Main screen interface
    
   <p align="center">
    <img src="images/screenshots/main_weather_new_yourk.png" width="250"/> <img          src="images/screenshots/main_weather_warsaw.png" width="250"/> <img  src="images/screenshots/main_weather_dubai.png" width="250"/> 
   </p>

- Main elements
    - App Bar
    <p align="center">
    <img src="images/screenshots/main_app_bar.png" width="500"/> 
   </p>
        - Navigation drawer toggle
        - search button
        - local time for current location in seconds
        - timezone name for current location
        - Header name for current location
        - Subheader name for current location
        - _Yahoo_ logo (attribution requirements)
        - _Floating action button_ for saving/editing location
        - information about refresh appearing with _Swipe To Refresh_ button
        
   - Current weather conditions
    <p align="center">
    <img src="images/screenshots/main_current_weather.png" width="500"/> 
   </p>
        - text information about current conditions with proper icon
        - current temperature
        - lowest and highest temperature for current day
        - wind chill
    
    - Detailed weather conditions
    <p align="center">
    <img src="images/screenshots/main_detailed_weather.png" width="500"/> 
   </p>
        - information about sunrise and sunset time
        - information about current sun position between sunset nd sunrise
        - current wind direction (pointer points real wind direction)
        - current wind speed
        - current humidity level
        - current atmospheric pressure
        - current visibility
    
    - Forecast
    <p align="center">
    <img src="images/screenshots/main_forecast.png" width="500"/> 
   </p>
        - general weather condions visualised by icons
        - name of 4 next days
        - maximum and minimum temperature for 4 next days
    
- Dynamic layout change
    <p align="center">
    <img src="images/screenshots/main_day_theme.png" width="250"/> <img src="images/screenshots/main_night_theme.png" width="250"/> 
   </p>
    Thanks to additional UI thread it is possible to dynamically change layout appearance. Every second _Runnable_ object, wchich was passed to the thread initializing method, is rerunned to reload layout. It is used for displaying changes related with time and sun position.
    
   <center>   
   
    |Element | time interval |
    |:------------ | :------------ |
    |clock | 1 sec|
    |sun position on a line | 1 min|
    |day/night theme | twice a day|
    |sunrise/sunset time and icon position swich | twice a day|
    |sun/moon icon swich | twice a day|
    
    </center>  
   
- Measurement system
    <p align="center">
    <img src="images/screenshots/main_units_euro.png" width="250"/> <img          src="images/screenshots/main_units_us.png" width="250"/> 
   </p>
   Application gives the user a possibility to change measurement system for following elements:
   - temperature: C/F degrees
   - wind speed: kmph / mph
   - visibility distance: km/mi
   - atmospheric pressure: hPa/mmHg
   - time format: 12h/24h
   
   User can change units in application settings available in _Navigation drawer_.
   
- Exit dialog
<p align="center">
    <img src="images/screenshots/main_exit.png" width="250"/> 
   </p>
   
    When the user pushes the back button there is a confirmation dialog showing up. This solution prevents from accidentaly leaving the application.

#### 2. Main screen intents
- Maps intent
When the user clicks on the current location name, there is a dialog showing up. After confirmation user is redirected to Google Maps.
    <p align="center">
    <img src="images/screenshots/main_intent_maps.png" width="250"/> 
   </p>
   
- Web intent
    According to Yahoo Weather API usage requirements, after clicking the Yahoo logo user is redirected to yahoo.com website. Yahoo Weather website with current location weather conditions is opened after choosing any of the weather information located on the main screen.
    <p align="center">
    <img src="images/screenshots/main_intent_yahoo.png" width="250"/> <img          src="images/screenshots/main_intent_yahoo_weather.png" width="250"/>
   </p>

#### 3. Location search

User has the possibility to search the weather information for every location provided by Yahoo Weather. After choosing click icon in the top right corner of the screen a dialog pops up. Here, the user have to enter his desired location. Before he do this search button is inactive. After entering the location and clicking outside the _EditText_ location name is formatted - before and after spaces are deleted and first letter is converted to uppercase. 

   <p align="center">
    <img src="images/screenshots/main_search_empty.png" width="250"/> <img src="images/screenshots/main_search_input.png" width="250"/>     <img  src="images/screenshots/main_search_focus_outside.png" width="250"/> 
   </p>
   
   The next step is to send a request to the server. In case of successful response there is a dialog showed up with the information about searched location. If there was a problem with internet connection or the request is empy, proper message is displayed on the screen.

   <p align="center">
    <img src="images/screenshots/main_search_result.png" width="250"/> <img src="images/screenshots/main_search_no_weather.png" width="250"/> <img  src="images/screenshots/main_internet_failure.png" width="250"/> 
   </p>

#### 4. Navigation drawer
   Application has implemented the _Navigation Drawer_ with the application logo at the top and several additional options to choose:
   - Geolocalization
   - Favourites
   - Settings
   - Send feedback
   - About
   - Author
   
<p align="center">
        <img src="images/screenshots/main_navigation_drawer.png" width="250"/> 
    </p>
   
#### 5. Geolocalization
 
   After choosing the _Geolocalization_ option from the _Notification Drawer_ geolocalization process is started. At the beginning the user is asked to choose the geolocalization method if he didn't do this yet. 

   <p align="center">
    <img src="images/screenshots/main_geolocalization_methods.png" width="250"/> 
   </p>
   
   Next, if the device has at least API level 24 the user has to grant the permissions to use the localization. 
   
   <p align="center">
    <img src="images/screenshots/main_permissions_request.png" width="250"/> <img          src="images/screenshots/main_permissions_denied.png" width="250"/>
   </p>
   
   In case of inaccessibility of the provider selected by the user, the other one is going to be used for retrieving current device coordinates. This information is showed up in a dialog.
   
   <p align="center">
    <img src="images/screenshots/main_geolocalization_network_unavailable.png" width="250"/> <img 
    src="images/screenshots/main_geolocalization_gps_unavailable.png" width="250"/>
   </p>
   
   If there is no provider available or there is a security exception dialog with a proper information is opened.
   
   <p align="center">
    <img src="images/screenshots/main_geolocalization_failure.png" width="250"/> <img          src="images/screenshots/main_geolocalization_security_exception.png" width="250"/>
   </p>
   
   If all of the needed conditions have been met the process of obtaining the position is started.
   
   <p align="center">
    <img src="images/screenshots/main_geolocalization_step_1.png" width="250"/> 
   </p>
   
   Coordinates retrieved in the previous procedure are used to find the location adress by using _Google Maps API_. 
   
   <p align="center">
    <img src="images/screenshots/main_geolocalization_step_2.png" width="250"/> 
   </p>
   
   If there is a problem with internet connection or a server request there is a dialog displayed with a proper information.
   
   <p align="center">
    <img src="images/screenshots/main_weather_failure_server.png" width="250"/> <img          src="images/screenshots/main_internet_failure.png" width="250"/>
   </p>
   
   The last step is downloading the weather information for found location.
   
   <p align="center">
    <img src="images/screenshots/main_geolocalization_step_3.png" width="250"/> 
   </p>
   
   If this operation is succeeded all the weather information is loaded in the main screen. In case of failure dialog with proper information is showed up. 
   
   <p align="center">
    <img src="images/screenshots/main_geolocalization_weather_failure.png" width="250"/> <img src="images/screenshots/main_internet_failure.png" width="250"/>
   </p>

#### 6. Favourites

User has the possibility to save his favourite location's. After selecting the _Floating Action Button_ whith the star icon on it there is shown a saving dialog with the option of changing the header and subheader location name and setting this location as a defeault. User can always edit saved location by selecting the same _Floating Action Button_, but now with the edit icon on it.   

<p align="center">
    <img src="images/screenshots/main_save_location_unedited.png" width="250"/> <img          src="images/screenshots/main_save_location_empty_subheader.png" width="250"/> <img  src="images/screenshots/main_edit_location.png" width="250"/> 
   </p>
   
When the user save location the icon of _Floating Action Button_ is changed and the _Favourites_ option in _Navigation Drawer_ is highlighted.   
   
   <p align="center">
    <img src="images/screenshots/main_favourites_before_save.png" width="250"/> <img          src="images/screenshots/main_favourites_before_save_nav.png" width="250"/>
   </p>
   
   <p align="center">
    <img src="images/screenshots/main_favourites_after_save.png" width="250"/> <img          src="images/screenshots/main_favourites_after_save_nav.png" width="250"/>
   </p>
   
   If the user hasn't add any location yet, after selecting _Favourites_ option in _Navigation Drawer_ there is displayed with a proper information. In the other case a dialog with scrollable favourites list is shown.
   
   <p align="center">
    <img src="images/screenshots/main_favourites_empty_list.png" width="250"/> <img          src="images/screenshots/main_favourites_list.png" width="250"/>
   </p>

#### 7. Other dialogs in Navigation Drawer
- Send feedback
User has the possibility to send the e-mail to the author of this application. After long clicking on the author's e-mail address it is copied to clipboard, which is showed by the _Toast_ message. After clicking _Send_ em-mail intent is used and an e-mail application is opened. If the user doesn't have the necessary applciation he is informed about this in dialog. In this situation he can copy the author's e-mail address to use it e.g. in a browser.

<p align="center">
    <img src="images/screenshots/main_nav_drawer_feedback.png" width="250"/> 
   </p>
   
- About
After selecting _About_ option there is shown a dialog with a basic information about _MatWeather_. All underlined elements in _Additional information_ are clickable hiperlinks.

<p align="center">
    <img src="images/screenshots/main_nav_drawer_about.png" width="250"/> 
   </p>

- Author
In _Author_ section the user can find the information about the Author's e-mail address and the _GitHub_ repositories. After choosing one of those options e-mail or web intent is opened.
<p align="center">
    <img src="images/screenshots/main_nav_drawer_author.png" width="250"/> 
   </p>
  
### Settings
After selecting the _Settings_ option in _Navigation Drawer_ a _Settings_ activity is opened.
<p align="center">
    <img src="images/screenshots/settings_main.png" width="250"/> 
   </p>
   
- Settings dialogs
Every dialog is created different class. All classes extends _CustomDialogPreference_ class which extends _DialogPreference_ class. Changes are saved to _SharedPreferences_ after clicking _Select_ button. 
    <p align="center">
    <img src="images/screenshots/settings_defeault_location.png" width="250"/> <img          src="images/screenshots/settings_geolocalization_method.png" width="250"/> <img  src="images/screenshots/settings_language_version.png" width="250"/> 
   </p>
   
- Units subsection
After selecting _Units_ option there is opened a _Units_ activity, where the user can change the measurement system. All units preferences dialogs extends _UnitsDialogPreference_ class, which extends _CustomDialogPreference_ class.
    <p align="center">
    <img src="images/screenshots/settings_units.png" width="250"/>  <img  src="images/screenshots/settings_units_example.png" width="250"/> 
   </p>





