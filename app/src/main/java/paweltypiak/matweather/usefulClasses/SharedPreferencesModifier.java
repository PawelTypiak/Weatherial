package paweltypiak.matweather.usefulClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.StringTokenizer;
import paweltypiak.matweather.R;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesModifier {
    private static SharedPreferences sharedPreferences;

    public static SharedPreferences getSharedPreferences(Context context){
        if(sharedPreferences==null) {
            sharedPreferences = context.getSharedPreferences(
                    context.getString(R.string.shared_preferences_name_key), MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static boolean getIsFirstLaunch(Context context){
        boolean isFirstLaunch=getSharedPreferences(context).getBoolean(context.getString(R.string.shared_preferences_is_first_launch_key),true);
        return isFirstLaunch;
    }

    public static void setNextLaunch(Context context){
        getSharedPreferences(context).edit().putBoolean(context.getString(R.string.shared_preferences_is_first_launch_key),false).commit();
    }

    public static int getLanguageVersion(Context context){
        int languageVersion=getSharedPreferences(context).getInt(context.getString(R.string.shared_preferences_language_version_key), -1);
        return languageVersion;
    }

    public static void setLanguage(Context context, int option){
        getSharedPreferences(context).edit().putInt(context.getString(R.string.shared_preferences_language_version_key), option).commit();
    }

    public static int[] getUnits(Context context){
        String unitsString = getSharedPreferences(context).getString(context.getString(R.string.shared_preferences_units_key), "-1,-1,-1,-1");
        int [] units=new int [4];
        StringTokenizer stringTokenizer = new StringTokenizer(unitsString, ",");
        for (int i = 0; i < units.length; i++) {
            units[i] = Integer.parseInt(stringTokenizer.nextToken());
        }
        return units;
    }

    public static void setUnits(Context context, int []unitsArray){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < unitsArray.length; i++) {
            stringBuilder.append(unitsArray[i]).append(",");
        }
        getSharedPreferences(context).edit().putString(context.getString(R.string.shared_preferences_units_key), stringBuilder.toString()).commit();
    }

    public static int getGeolocalizationMethod(Context context){
        int geolocalizationMethod=getSharedPreferences(context).getInt(context.getString(R.string.shared_preferences_geolocalization_method_key),-1);
        return geolocalizationMethod;
    }

    public static void setGeolocalizationMethod(Context context, int option){
        getSharedPreferences(context).edit().putInt(context.getString(R.string.shared_preferences_geolocalization_method_key),option).commit();
    }

    public static String getDefaultLocation(Context context){
        String defaultLocation=getSharedPreferences(context).getString(context.getString(R.string.shared_preferences_default_location_key),null);
        Log.d("SharedPreferences", "defeaultLocation: "+defaultLocation);
        return defaultLocation;
    }

    public static boolean isDefaultLocationConstant(Context context){
        String defaultLocation= getDefaultLocation(context);
        if(defaultLocation==null) return false;
        else return true;
    }

    public static void setDefaultLocationConstant(Context context, String locationAddress){
        getSharedPreferences(context).edit().putString(context.getString(R.string.shared_preferences_default_location_key), locationAddress).commit();
    }

    public static void setDefaultLocationGeolocalization(Context context){
        getSharedPreferences(context).edit().putString(context.getString(R.string.shared_preferences_default_location_key), null).commit();
    }

    public static String[] getFavouriteLocationsNames(Context context){
        String favouritesString = getSharedPreferences(context).getString(context.getString(R.string.shared_preferences_favourite_locations_names_key), "");
        StringTokenizer stringTokenizer = new StringTokenizer(favouritesString, "|");
        int numberOfFavourites=stringTokenizer.countTokens();
        String favourites[]=new String[numberOfFavourites];
        for (int i = 0; i < numberOfFavourites; i++) {
            favourites[i] = stringTokenizer.nextToken();
            Log.d("SharedPreferences", "favourites names: "+ favourites[i]);
        }
        return favourites;
    }

    public static void setFavouriteLocationNames(Context context,String namesString){
        getSharedPreferences(context).edit().putString(context.getString(R.string.shared_preferences_favourite_locations_names_key), namesString).commit();
    }

    public static String[] getFavouriteLocationsAddresses(Context context){
        String favouritesString = getSharedPreferences(context).getString(context.getString(R.string.shared_preferences_favourite_locations_addresses_key), "");
        StringTokenizer stringTokenizer = new StringTokenizer(favouritesString, "|");
        int numberOfFavourites=stringTokenizer.countTokens();
        String favourites[]=new String[numberOfFavourites];
        for (int i = 0; i < numberOfFavourites; i++) {
            favourites[i] = stringTokenizer.nextToken();
            Log.d("SharedPreferences", "favourites addresses: "+ favourites[i]);
        }
        return favourites;
    }

    public static void setFavouriteLocationAddresses(Context context,String adressesString){
        getSharedPreferences(context).edit().putString(context.getString(R.string.shared_preferences_favourite_locations_addresses_key), adressesString).commit();
    }
}
