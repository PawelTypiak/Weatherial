package paweltypiak.matweather.usefulClasses;

import android.app.Activity;
import android.util.Log;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class FavouritesEditor {
    private static int choosenLocationId;
    private static String[] favouritesHeaderNames;
    private static String[] favouritesSubheaderNames;


    public static void setChoosenLocationID(int id){
        choosenLocationId=id;
    }

    public static int getChoosenLocationID(){
        return choosenLocationId;
    }

    public static int getNumberOfFavourites(Activity activity){
        return SharedPreferencesModifier.getFavouriteLocationsNames(activity).length;
    }

    public static String getChoosenFavouriteLocationAddress(Activity activity){
        String[] addresses=SharedPreferencesModifier.getFavouriteLocationsAddresses(activity);
        Log.d("adres", addresses[choosenLocationId]);
        return addresses[choosenLocationId];
    }

    private static String makeLocationsDialogName(String header, String subheader){
        String name="<b>"+header+"</b>";
        if(!subheader.equals(""))name=name+", "+subheader;
        return name;
    }

    public static void setAppBarForFavouriteLocation(Activity activity, int currentId){
        Log.d("id", "setAppBarForFavouriteLocation: "+currentId);

        UsefulFunctions.setAppBarStrings(activity,favouritesHeaderNames[currentId],favouritesSubheaderNames[currentId]);
    }

    private static void getFavouriteLocationsNamesList(Activity activity){
        String[] favourites=SharedPreferencesModifier.getFavouriteLocationsNames(activity);
        int favouritesSize=favourites.length;
        favouritesHeaderNames=new String[favouritesSize];
        favouritesSubheaderNames=new String[favouritesSize];
        for(int i=0;i<favouritesSize;i++){
            StringTokenizer stringTokenizer = new StringTokenizer(favourites[i], "%");
            String locationPartName[]={"",""};
            int tokenizerSize=stringTokenizer.countTokens();
            Log.d("tokinizersize", ""+stringTokenizer.countTokens());
            for (int j = 0; j < tokenizerSize; j++) {
                locationPartName[j] = stringTokenizer.nextToken();
                Log.d("partname", ""+locationPartName[j]);
            }
            favouritesHeaderNames[i]=locationPartName[0];
            favouritesSubheaderNames[i]=locationPartName[1];
        }
    }

    public static List<String> getFavouriteLocationsNamesDialogList(Activity activity){
        getFavouriteLocationsNamesList(activity);
        int size=favouritesHeaderNames.length;
        String locationNames[]=new String[size];
        for(int i=0;i<size;i++){
            locationNames[i]=makeLocationsDialogName(favouritesHeaderNames[i],favouritesSubheaderNames[i]);
        }
        List<String> favouriteLocationsNamesDialogList= Arrays.asList(locationNames);
        return favouriteLocationsNamesDialogList;
    }

    public static void saveNewFavouriteLocationName(Activity activity,String headerString,String subheaderString){
        String favourites[]= SharedPreferencesModifier.getFavouriteLocationsNames(activity);
        StringBuilder stringBuilder=UsefulFunctions.buildStringFromStringArray(favourites);
        String locationName=headerString+"%"+subheaderString;
        stringBuilder.append(locationName).append("|");
        String favouritesNamesString=stringBuilder.toString();
        Log.d("string_names_save", ""+favouritesNamesString);
        SharedPreferencesModifier.setFavouriteLocationNames(activity,favouritesNamesString);
    }

    public static void saveNewFavouriteLocationAddress(Activity activity,String currentLocationAddressString){
        if(currentLocationAddressString==null){
            String currentLocationHeaderString=UsefulFunctions.getCurrentLocationAddress()[0];
            String currentLocationSubheaderString=UsefulFunctions.getCurrentLocationAddress()[1];
            currentLocationAddressString=currentLocationHeaderString+", "+currentLocationSubheaderString;
        }

        String favourites[]= SharedPreferencesModifier.getFavouriteLocationsAddresses(activity);
        StringBuilder stringBuilder=UsefulFunctions.buildStringFromStringArray(favourites);
        stringBuilder.append(currentLocationAddressString).append("|");
        String favouritesAddressesString=stringBuilder.toString();
        Log.d("string_Address_save", ""+favouritesAddressesString);
        SharedPreferencesModifier.setFavouriteLocationAddresses(activity,favouritesAddressesString);

    }

    public static void saveNewFavouriteLocationCoordinates(Activity activity,String currentLocationCoordinatesString){
        if(currentLocationCoordinatesString==null){
            String currentLocationLatitude=UsefulFunctions.getCurrentLocationCoordinates()[0];
            String currentLocationLongitude=UsefulFunctions.getCurrentLocationCoordinates()[1];
            currentLocationCoordinatesString=currentLocationLatitude+"%"+currentLocationLongitude;
        }

        String favourites[]= SharedPreferencesModifier.getFavouriteLocationsCoordinates(activity);
        StringBuilder stringBuilder=UsefulFunctions.buildStringFromStringArray(favourites);
        stringBuilder.append(currentLocationCoordinatesString).append("|");
        String favouritesCoordinateString=stringBuilder.toString();
        Log.d("string_Coordinate_save", ""+favouritesCoordinateString);
        SharedPreferencesModifier.setFavouriteLocationCoordinates(activity,favouritesCoordinateString);
    }

    public static void saveNewFavouritesItem(Activity activity,String headerString, String subheaderString, String currentLocationNameString,String currentLocationCoordinatesString){
        FavouritesEditor.saveNewFavouriteLocationName(activity,headerString,subheaderString);
        FavouritesEditor.saveNewFavouriteLocationAddress(activity,currentLocationNameString);
        FavouritesEditor.saveNewFavouriteLocationCoordinates(activity,currentLocationCoordinatesString);
    }

    /*public static void saveFirstLaunchNewFavouritesItem(Activity activity,String headerString, String subheaderString, String currentLocationNameString,String currentLocationCoordinatesString){
        FavouritesEditor.saveNewFavouriteLocationName(activity,headerString,subheaderString);
        FavouritesEditor.saveNewFavouriteLocationAddress(activity,currentLocationNameString);
        FavouritesEditor.saveNewFavouriteLocationCoordinates(activity,currentLocationCoordinatesString);
    }*/

    public static void editFavouriteLocationName(Activity activity,String headerString, String subheaderString){
        String namesString=getFavouritesStringAfterEdit(activity,headerString,subheaderString);
        Log.d("nowy string", namesString);
        SharedPreferencesModifier.setFavouriteLocationNames(activity,namesString);
    }

    private static String getFavouritesStringAfterEdit(Activity activity, String headerString, String subheaderString){
        String favouritesNames[]=SharedPreferencesModifier.getFavouriteLocationsNames(activity);
        int id= getCurrentFavouriteLocationId(activity);
        String locationName=headerString+"%"+subheaderString;
        Log.d("nowa nazwa", locationName);
        favouritesNames[id]=locationName;
        StringBuilder stringBuilder=UsefulFunctions.buildStringFromStringArray(favouritesNames);
        String newFavouritesString=stringBuilder.toString();
        return  newFavouritesString;
    }

    public static void deleteFavouritesItem(Activity activity){
        String favouritesNames[]=SharedPreferencesModifier.getFavouriteLocationsNames(activity);
        String favouritesAddresses[]=SharedPreferencesModifier.getFavouriteLocationsAddresses(activity);
        String favouritesCoordinates[]=SharedPreferencesModifier.getFavouriteLocationsCoordinates(activity);
        String namesString= getFavouritesStringAfterRemove(activity,favouritesNames);
        String addressesString= getFavouritesStringAfterRemove(activity,favouritesAddresses);
        String coordinatesString= getFavouritesStringAfterRemove(activity,favouritesCoordinates);
        SharedPreferencesModifier.setFavouriteLocationNames(activity,namesString);
        SharedPreferencesModifier.setFavouriteLocationAddresses(activity,addressesString);
        SharedPreferencesModifier.setFavouriteLocationCoordinates(activity,coordinatesString);
    }

    private static String getFavouritesStringAfterRemove(Activity activity, String[] favourites){
        List<String> favouritesList = new LinkedList<String>(Arrays.asList(favourites));
        favourites=new String[favouritesList.size()-1];
        int id= getCurrentFavouriteLocationId(activity);
        int i=0;
        for (Iterator<String> iter = favouritesList.listIterator(); iter.hasNext(); ) {
            String locationItem = iter.next();
            if (i==id) {
                Log.d("item removed", locationItem);
                iter.remove();
            }
            i++;
        }
        i=0;
        for (Iterator<String> iter = favouritesList.listIterator(); iter.hasNext(); ) {
            String locationItem = iter.next();
            favourites[i]=locationItem;
            i++;
        }
        StringBuilder stringBuilder=UsefulFunctions.buildStringFromStringArray(favourites);
        String newFavouritesString=stringBuilder.toString();
        return  newFavouritesString;
    }

    public static void setLayoutForFavourites(Activity activity){
        int id=getCurrentFavouriteLocationId(activity);
        getFavouriteLocationsNamesList(activity);
        setAppBarForFavouriteLocation(activity,id);
        UsefulFunctions.setfloatingActionButtonOnClickIndicator(activity,2);
        UsefulFunctions.checkNavigationDrawerMenuItem(activity,2);
    }

    private static int getCurrentFavouriteLocationId(Activity activity){
        String favouritesCoordinates[]= SharedPreferencesModifier.getFavouriteLocationsCoordinates(activity);
        String coordinates[]=new String [2];
        int id=0;
        for(int i=0;i<favouritesCoordinates.length;i++){
            StringTokenizer stringTokenizer = new StringTokenizer(favouritesCoordinates[i], "%");
            for (int j = 0; j < 2; j++) {
                coordinates[j] = stringTokenizer.nextToken();
            }
            if(coordinates[0].equals(UsefulFunctions.getCurrentLocationCoordinates()[0])
                    && coordinates[1].equals(UsefulFunctions.getCurrentLocationCoordinates()[1])) id=i;
        }
        return id;
    }

    public static boolean areCoordinatesEqual(Activity activity){
        String favouritesCoordinates[]= SharedPreferencesModifier.getFavouriteLocationsCoordinates(activity);
        boolean isEqual=false;
        String coordinates[]=new String [2];
        for(int i=0;i<favouritesCoordinates.length;i++){
            StringTokenizer stringTokenizer = new StringTokenizer(favouritesCoordinates[i], "%");
            for (int j = 0; j < 2; j++) {
                coordinates[j] = stringTokenizer.nextToken();
            }
            if(coordinates[0].equals(UsefulFunctions.getCurrentLocationCoordinates()[0])
                    && coordinates[1].equals(UsefulFunctions.getCurrentLocationCoordinates()[1])) isEqual=true;
        }
        return isEqual;
    }

    public static boolean isFirstLocationEqual(Activity activity){
        String currentLocationHeaderString=UsefulFunctions.getCurrentLocationAddress()[0];
        String currentLocationSubheaderString=UsefulFunctions.getCurrentLocationAddress()[1];
        String currentLocationAddress=currentLocationHeaderString+", "+currentLocationSubheaderString;
        String firstLocation=SharedPreferencesModifier.getFirstLocation(activity);
        if(firstLocation.equals(currentLocationAddress)) {
            return true;
        }
        else return false;
    }
}