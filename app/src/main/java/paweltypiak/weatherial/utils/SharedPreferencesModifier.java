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
package paweltypiak.weatherial.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.StringTokenizer;
import paweltypiak.weatherial.R;
import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesModifier {

    private static SharedPreferences sharedPreferences;

    private static SharedPreferences getSharedPreferences(Context context){
        if(sharedPreferences==null) {
            sharedPreferences = context.getSharedPreferences(
                    context.getString(R.string.shared_preferences_name_key), MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static boolean getIsFirstLaunch(Context context){
        return getSharedPreferences(context).getBoolean(context.getString(R.string.shared_preferences_is_first_launch_key),true);
    }

    public static void setNextLaunch(Context context){
        getSharedPreferences(context).edit().putBoolean(context.getString(R.string.shared_preferences_is_first_launch_key),false).apply();
    }

    public static int getLanguageVersion(Context context){
        return getSharedPreferences(context).getInt(context.getString(R.string.shared_preferences_language_version_key), -1);
    }

    public static void setLanguage(Context context, int option){
        getSharedPreferences(context).edit().putInt(context.getString(R.string.shared_preferences_language_version_key), option).apply();
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
        getSharedPreferences(context).edit().putString(context.getString(R.string.shared_preferences_units_key), stringBuilder.toString()).apply();
    }

    public static int getGeolocalizationMethod(Context context){
        return getSharedPreferences(context).getInt(context.getString(R.string.shared_preferences_geolocalization_method_key),-1);
    }

    public static void setGeolocalizationMethod(Context context, int option){
        getSharedPreferences(context).edit().putInt(context.getString(R.string.shared_preferences_geolocalization_method_key),option).apply();
    }

    public static String getDefaultLocation(Context context){
        return getSharedPreferences(context).getString(context.getString(R.string.shared_preferences_default_location_key),null);
    }

    public static boolean isDefaultLocationConstant(Context context){
        String defaultLocation= getDefaultLocation(context);
        if(defaultLocation==null) return false;
        else return true;
    }

    public static void setDefaultLocationConstant(Context context, String locationAddress){
        getSharedPreferences(context).edit().putString(context.getString(R.string.shared_preferences_default_location_key), locationAddress).apply();
    }

    public static void setDefaultLocationGeolocalization(Context context){
        getSharedPreferences(context).edit().putString(context.getString(R.string.shared_preferences_default_location_key), null).apply();
    }

    public static String[] getFavouriteLocationsNames(Context context){
        String favouritesString = getSharedPreferences(context).getString(context.getString(R.string.shared_preferences_favourite_locations_names_key), "");
        StringTokenizer stringTokenizer = new StringTokenizer(favouritesString, "|");
        int numberOfFavourites=stringTokenizer.countTokens();
        String favourites[]=new String[numberOfFavourites];
        for (int i = 0; i < numberOfFavourites; i++) {
            favourites[i] = stringTokenizer.nextToken();
        }
        return favourites;
    }

    public static void setFavouriteLocationNames(Context context,String namesString){
        getSharedPreferences(context).edit().putString(context.getString(R.string.shared_preferences_favourite_locations_names_key), namesString).apply();
    }

    public static String[] getFavouriteLocationsAddresses(Context context){
        String favouritesString = getSharedPreferences(context).getString(context.getString(R.string.shared_preferences_favourite_locations_addresses_key), "");
        StringTokenizer stringTokenizer = new StringTokenizer(favouritesString, "|");
        int numberOfFavourites=stringTokenizer.countTokens();
        String favourites[]=new String[numberOfFavourites];
        for (int i = 0; i < numberOfFavourites; i++) {
            favourites[i] = stringTokenizer.nextToken();
        }
        return favourites;
    }

    public static void setFavouriteLocationAddresses(Context context,String adressesString){
        getSharedPreferences(context).edit().putString(context.getString(R.string.shared_preferences_favourite_locations_addresses_key), adressesString).apply();
    }
}
