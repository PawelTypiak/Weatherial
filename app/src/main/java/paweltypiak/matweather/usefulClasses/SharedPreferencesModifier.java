package paweltypiak.matweather.usefulClasses;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.StringTokenizer;
import paweltypiak.matweather.R;

public class SharedPreferencesModifier {
    private static SharedPreferences sharedPreferences;

    public static SharedPreferences getSharedPreferences(Activity activity){
        if(sharedPreferences==null) {
            sharedPreferences = activity.getSharedPreferences(
                    activity.getString(R.string.shared_preferences_name_key), activity.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static boolean getIsFirstLaunch(Activity activity){
        boolean isFirstLaunch=getSharedPreferences(activity).getBoolean(activity.getString(R.string.shared_preferences_is_first_launch_key),true);
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
        int [] units=new int [5];
        StringTokenizer stringTokenizer = new StringTokenizer(unitsString, ",");
        for (int i = 0; i < units.length; i++) {
            units[i] = Integer.parseInt(stringTokenizer.nextToken());
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

    public static int getGeolocalizationMethod(Activity activity){
        int geolocalizationMethod=getSharedPreferences(activity).getInt(activity.getString(R.string.shared_preferences_geolocalization_method_key),0);
        return geolocalizationMethod;
    }

    public static void setGeolocalizationMethod(Activity activity, int option){
        getSharedPreferences(activity).edit().putInt(activity.getString(R.string.shared_preferences_geolocalization_method_key),option).commit();
    }

    public static String getDefeaultLocation(Activity activity){
        String defeaultLocation=getSharedPreferences(activity).getString(activity.getString(R.string.shared_preferences_defeault_location_key),null);
        Log.d("SharedPreferences", "defeaultLocation: "+defeaultLocation);
        return defeaultLocation;
    }

    public static boolean isDefeaultLocationConstant(Activity activity){
        String defeaultLocation= getDefeaultLocation(activity);
        if(defeaultLocation.equals("")) return false;
        else return true;
    }

    public static void setDefeaultLocationConstant(Activity activity, String locationName){
        getSharedPreferences(activity).edit().putString(activity.getString(R.string.shared_preferences_defeault_location_key), locationName).commit();
    }

    public static void setDefeaultLocationGeolocalization(Activity activity){
        getSharedPreferences(activity).edit().putString(activity.getString(R.string.shared_preferences_defeault_location_key), "").commit();
    }

    public static String[] getFavouriteLocationsNames(Activity activity){
        String favouritesString = getSharedPreferences(activity).getString(activity.getString(R.string.shared_preferences_favourite_locations_names_key), "");
        StringTokenizer stringTokenizer = new StringTokenizer(favouritesString, "|");
        int numberOfFavourites=stringTokenizer.countTokens();
        String favourites[]=new String[numberOfFavourites];
        for (int i = 0; i < numberOfFavourites; i++) {
            favourites[i] = stringTokenizer.nextToken();
            Log.d("SharedPreferences", "favourites names: "+ favourites[i]);
        }
        return favourites;
    }

    public static void setFavouriteLocationNames(Activity activity,String namesString){
        getSharedPreferences(activity).edit().putString(activity.getString(R.string.shared_preferences_favourite_locations_names_key), namesString).commit();
    }

    public static String[] getFavouriteLocationsAddresses(Activity activity){
        String favouritesString = getSharedPreferences(activity).getString(activity.getString(R.string.shared_preferences_favourite_locations_addresses_key), "");
        StringTokenizer stringTokenizer = new StringTokenizer(favouritesString, "|");
        int numberOfFavourites=stringTokenizer.countTokens();
        String favourites[]=new String[numberOfFavourites];
        for (int i = 0; i < numberOfFavourites; i++) {
            favourites[i] = stringTokenizer.nextToken();
            Log.d("SharedPreferences", "favourites addresses: "+ favourites[i]);
        }
        return favourites;
    }

    public static void setFavouriteLocationAddresses(Activity activity,String adressesString){
        getSharedPreferences(activity).edit().putString(activity.getString(R.string.shared_preferences_favourite_locations_addresses_key), adressesString).commit();
    }

    public static String[] getFavouriteLocationsCoordinates(Activity activity){
        String favouritesString = getSharedPreferences(activity).getString(activity.getString(R.string.shared_preferences_favourite_locations_coordinates_key), "");
        StringTokenizer stringTokenizer = new StringTokenizer(favouritesString, "|");
        int numberOfFavourites=stringTokenizer.countTokens();
        String favourites[]=new String[numberOfFavourites];
        for (int i = 0; i < numberOfFavourites; i++) {
            favourites[i] = stringTokenizer.nextToken();
            Log.d("SharedPreferences", "favourites coordinates: "+ favourites[i]);
        }
        return favourites;
    }

    public static void setFavouriteLocationCoordinates(Activity activity,String coordinatesString){
        getSharedPreferences(activity).edit().putString(activity.getString(R.string.shared_preferences_favourite_locations_coordinates_key), coordinatesString).commit();
    }
}
