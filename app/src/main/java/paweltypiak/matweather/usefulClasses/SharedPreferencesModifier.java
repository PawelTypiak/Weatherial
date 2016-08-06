package paweltypiak.matweather.usefulClasses;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import paweltypiak.matweather.R;

public class SharedPreferencesModifier {
    private static SharedPreferences sharedPreferences;

    public static SharedPreferences getSharedPreferences(Activity activity){
        if(sharedPreferences==null) {
            sharedPreferences = activity.getSharedPreferences(
                    activity.getString(R.string.shared_preferences_key_name), activity.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static boolean getIsFirstLaunch(Activity activity){
        boolean isFirstLaunch=getSharedPreferences(activity).getBoolean(activity.getString(R.string.shared_preferences_is_first_launch_key),true);
        Log.d("isFirst", ""+isFirstLaunch);
        return isFirstLaunch;
    }

    public static void setNextLaunch(Activity activity){
        getSharedPreferences(activity).edit().putBoolean(activity.getString(R.string.shared_preferences_is_first_launch_key),false).commit();
    }

    public static int getLanguage(Activity activity){
        int language=getSharedPreferences(activity).getInt(activity.getString(R.string.shared_preferences_language_key), 0);
        return language;
    }

    public static void setLanguage(Activity activity, int option){
        getSharedPreferences(activity).edit().putInt(activity.getString(R.string.shared_preferences_language_key), option).commit();
    }

    public static int[] getUnits(Activity activity){
        String unitsString = getSharedPreferences(activity).getString(activity.getString(R.string.shared_preferences_units_key), "0,0,0,0,0,");
        Log.d("units_useful_func", unitsString);
        int [] units=new int [5];
        StringTokenizer stringTokenizer = new StringTokenizer(unitsString, ",");
        for (int i = 0; i < units.length; i++) {
            units[i] = Integer.parseInt(stringTokenizer.nextToken());
            Log.d("units_static", ""+ units[i]);
        }
        return units;
    }

    public static void setUnits(Activity activity, int []unitsArray){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < unitsArray.length; i++) {
            stringBuilder.append(unitsArray[i]).append(",");
        }
        getSharedPreferences(activity).edit().putString(activity.getString(R.string.shared_preferences_units_key), stringBuilder.toString()).commit();
    }

    public static int getLocalizationOptionKey(Activity activity){
        int localizationOptionKey=getSharedPreferences(activity).getInt(activity.getString(R.string.shared_preferences_localization_option_key),0);
        Log.d("localizationOption", ""+localizationOptionKey);
        return localizationOptionKey;
    }

    public static void setLocalizationOptionKey(Activity activity,int option){
        getSharedPreferences(activity).edit().putInt(activity.getString(R.string.shared_preferences_localization_option_key),option);
    }

    public static String getFirstLocation(Activity activity){
        String firstLocation=getSharedPreferences(activity).getString(activity.getString(R.string.shared_preferences_first_location_key),null);
        Log.d("FirstLocation", ""+firstLocation);
        return firstLocation;
    }

    public static void setLocation(Activity activity, String locationName){
        getSharedPreferences(activity).edit().putString(activity.getString(R.string.shared_preferences_first_location_key), locationName).commit();
    }

    public static void setGeolocalization(Activity activity){
        getSharedPreferences(activity).edit().putString(activity.getString(R.string.shared_preferences_first_location_key), null).commit();
    }

    public static String[] getFavouriteLocationsNames(Activity activity){
        String favouritesString = getSharedPreferences(activity).getString(activity.getString(R.string.shared_preferences_favourite_locations_names_key), "");
        Log.d("fav_names_string", favouritesString);
        StringTokenizer stringTokenizer = new StringTokenizer(favouritesString, "|");
        int numberOfLocations=stringTokenizer.countTokens();
        String favourites[]=new String[numberOfLocations];
        for (int i = 0; i < numberOfLocations; i++) {
            favourites[i] = stringTokenizer.nextToken();
            Log.d("favourite name", ""+ favourites[i]);
        }
        return favourites;
    }

    public static void setFavouriteLocationNames(Activity activity,String namesString){
        getSharedPreferences(activity).edit().putString(activity.getString(R.string.shared_preferences_favourite_locations_names_key), namesString).commit();
    }

    public static String[] getFavouriteLocationsAddresses(Activity activity){
        String favouritesString = getSharedPreferences(activity).getString(activity.getString(R.string.shared_preferences_favourite_locations_addresses_key), "");
        Log.d("fav_addresses_string", favouritesString);
        StringTokenizer stringTokenizer = new StringTokenizer(favouritesString, "|");
        int numberOfLocations=stringTokenizer.countTokens();
        String favourites[]=new String[numberOfLocations];
        for (int i = 0; i < numberOfLocations; i++) {
            favourites[i] = stringTokenizer.nextToken();
            Log.d("favourite address", ""+ favourites[i]);
        }
        return favourites;
    }

    public static void setFavouriteLocationAddresses(Activity activity,String adressesString){
        getSharedPreferences(activity).edit().putString(activity.getString(R.string.shared_preferences_favourite_locations_addresses_key), adressesString).commit();
    }

    public static String[] getFavouriteLocationsCoordinates(Activity activity){
        String favouritesString = getSharedPreferences(activity).getString(activity.getString(R.string.shared_preferences_favourite_locations_coordinates_key), "");
        Log.d("fav_coordinates_string", favouritesString);
        StringTokenizer stringTokenizer = new StringTokenizer(favouritesString, "|");
        int numberOfLocations=stringTokenizer.countTokens();
        String favourites[]=new String[numberOfLocations];
        for (int i = 0; i < numberOfLocations; i++) {
            favourites[i] = stringTokenizer.nextToken();
            Log.d("favourite coordinates", ""+ favourites[i]);
        }
        return favourites;
    }

    public static void setFavouriteLocationCoordinates(Activity activity,String coordinatesString){
        getSharedPreferences(activity).edit().putString(activity.getString(R.string.shared_preferences_favourite_locations_coordinates_key), coordinatesString).commit();
    }



}
