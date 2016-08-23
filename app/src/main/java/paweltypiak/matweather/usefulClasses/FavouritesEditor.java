package paweltypiak.matweather.usefulClasses;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class FavouritesEditor {
    private static int choosenFavouriteLocationId;
    private static String[] favouritesHeaderNames;
    private static String[] favouritesSubheaderNames;

    public static void setChoosenFavouriteLocationID(int id){
        choosenFavouriteLocationId =id;
    }

    public static int getChoosenFavouriteLocationID(){
        return choosenFavouriteLocationId;
    }

    public static int getNumberOfFavourites(Context context){
        return SharedPreferencesModifier.getFavouriteLocationsNames(context).length;
    }

    public static String getChoosenFavouriteLocationAddress(Context context){
        String[] addresses=SharedPreferencesModifier.getFavouriteLocationsAddresses(context);
        Log.d("favourites", "choosem favourite location address: "+addresses[choosenFavouriteLocationId]);
        return addresses[choosenFavouriteLocationId];
    }

    public static String getChoosenFavouriteLocationEditedName(){
        String header=favouritesHeaderNames[choosenFavouriteLocationId];
        String subheader=favouritesSubheaderNames[choosenFavouriteLocationId];
        String locationName= makeLocationNameWithBoldHeader(header,subheader);
        return locationName;
    }

    public static String getDefeaultLocationEditedName(Context context){
        int defeaultLocationId=getDefeaultLocationId(context);
        String header=favouritesHeaderNames[defeaultLocationId];
        String subheader=favouritesSubheaderNames[defeaultLocationId];
        String defeaultLocationName= makeLocationNameWithBoldHeader(header,subheader);
        return defeaultLocationName;
    }

    private static String makeLocationNameWithBoldHeader(String header, String subheader){
        String name="<b>"+header+"</b>";
        if(!subheader.equals(""))name=name+", "+subheader;
        return name;
    }

    public static void setAppBarForFavouriteLocation(Activity activity, int currentId){
        UsefulFunctions.setAppBarStrings(activity,favouritesHeaderNames[currentId],favouritesSubheaderNames[currentId]);
    }

    private static void getFavouriteLocationsNamesList(Context context){
        String[] favourites=SharedPreferencesModifier.getFavouriteLocationsNames(context);
        int favouritesSize=favourites.length;
        favouritesHeaderNames=new String[favouritesSize];
        favouritesSubheaderNames=new String[favouritesSize];
        for(int i=0;i<favouritesSize;i++){
            StringTokenizer stringTokenizer = new StringTokenizer(favourites[i], "%");
            String locationPartName[]={"",""};
            int tokenizerSize=stringTokenizer.countTokens();
            for (int j = 0; j < tokenizerSize; j++) {
                locationPartName[j] = stringTokenizer.nextToken();
            }
            favouritesHeaderNames[i]=locationPartName[0];
            favouritesSubheaderNames[i]=locationPartName[1];
        }
    }

    public static List<String> getFavouriteLocationsNamesDialogList(Context context){
        getFavouriteLocationsNamesList(context);
        int size=favouritesHeaderNames.length;
        String locationNames[]=new String[size];
        for(int i=0;i<size;i++){
            locationNames[i]= makeLocationNameWithBoldHeader(favouritesHeaderNames[i],favouritesSubheaderNames[i]);
        }
        List<String> favouriteLocationsNamesDialogList= Arrays.asList(locationNames);
        return favouriteLocationsNamesDialogList;
    }

    public static void saveNewFavouriteLocationName(Context context,String headerString,String subheaderString){
        String favourites[]= SharedPreferencesModifier.getFavouriteLocationsNames(context);
        StringBuilder stringBuilder=UsefulFunctions.buildStringFromStringArray(favourites);
        String locationName=headerString+"%"+subheaderString;
        stringBuilder.append(locationName).append("|");
        String favouritesNamesString=stringBuilder.toString();
        Log.d("favourites", "location name saved to favourites: "+favouritesNamesString);
        SharedPreferencesModifier.setFavouriteLocationNames(context,favouritesNamesString);
    }

    public static void saveNewFavouriteLocationAddress(Context context,String currentLocationAddressString){
        if(currentLocationAddressString==null){
            String currentLocationHeaderString=UsefulFunctions.getCurrentLocationAddress()[0];
            String currentLocationSubheaderString=UsefulFunctions.getCurrentLocationAddress()[1];
            currentLocationAddressString=currentLocationHeaderString+", "+currentLocationSubheaderString;
        }
        String favourites[]= SharedPreferencesModifier.getFavouriteLocationsAddresses(context);
        StringBuilder stringBuilder=UsefulFunctions.buildStringFromStringArray(favourites);
        stringBuilder.append(currentLocationAddressString).append("|");
        String favouritesAddressesString=stringBuilder.toString();
        Log.d("favourites", "location address saved to favourites: "+favouritesAddressesString);
        SharedPreferencesModifier.setFavouriteLocationAddresses(context,favouritesAddressesString);
    }

    public static void saveNewFavouriteLocationCoordinates(Context context,String currentLocationCoordinatesString){
        if(currentLocationCoordinatesString==null){
            String currentLocationLatitude=UsefulFunctions.getCurrentLocationCoordinates()[0];
            String currentLocationLongitude=UsefulFunctions.getCurrentLocationCoordinates()[1];
            currentLocationCoordinatesString=currentLocationLatitude+"%"+currentLocationLongitude;
        }
        String favourites[]= SharedPreferencesModifier.getFavouriteLocationsCoordinates(context);
        StringBuilder stringBuilder=UsefulFunctions.buildStringFromStringArray(favourites);
        stringBuilder.append(currentLocationCoordinatesString).append("|");
        String favouritesCoordinateString=stringBuilder.toString();
        Log.d("favourites", "location coordiates saved to favourites: "+favouritesCoordinateString);
        SharedPreferencesModifier.setFavouriteLocationCoordinates(context,favouritesCoordinateString);
    }

    public static void saveNewFavouritesItem(Context context,String headerString, String subheaderString, String currentLocationNameString,String currentLocationCoordinatesString){
        FavouritesEditor.saveNewFavouriteLocationName(context,headerString,subheaderString);
        FavouritesEditor.saveNewFavouriteLocationAddress(context,currentLocationNameString);
        FavouritesEditor.saveNewFavouriteLocationCoordinates(context,currentLocationCoordinatesString);
    }

    public static void editFavouriteLocationName(Context context,String headerString, String subheaderString){
        Log.d("favourites", "name edited: "+headerString+", "+subheaderString);
        String namesString=getFavouritesStringAfterEdit(context,headerString,subheaderString);
        SharedPreferencesModifier.setFavouriteLocationNames(context,namesString);
    }

    private static String getFavouritesStringAfterEdit(Context context, String headerString, String subheaderString){
        String favouritesNames[]=SharedPreferencesModifier.getFavouriteLocationsNames(context);
        int id= getCurrentFavouriteLocationId(context);
        String locationName=headerString+"%"+subheaderString;
        favouritesNames[id]=locationName;
        StringBuilder stringBuilder=UsefulFunctions.buildStringFromStringArray(favouritesNames);
        String newFavouritesString=stringBuilder.toString();
        return  newFavouritesString;
    }

    public static void deleteFavouritesItem(Context context){
        String favouritesNames[]=SharedPreferencesModifier.getFavouriteLocationsNames(context);
        String favouritesAddresses[]=SharedPreferencesModifier.getFavouriteLocationsAddresses(context);
        String favouritesCoordinates[]=SharedPreferencesModifier.getFavouriteLocationsCoordinates(context);
        String namesString= getFavouritesStringAfterRemove(context,favouritesNames);
        String addressesString= getFavouritesStringAfterRemove(context,favouritesAddresses);
        String coordinatesString= getFavouritesStringAfterRemove(context,favouritesCoordinates);
        SharedPreferencesModifier.setFavouriteLocationNames(context,namesString);
        SharedPreferencesModifier.setFavouriteLocationAddresses(context,addressesString);
        SharedPreferencesModifier.setFavouriteLocationCoordinates(context,coordinatesString);
    }

    private static String getFavouritesStringAfterRemove(Context context, String[] favourites){
        List<String> favouritesList = new LinkedList<String>(Arrays.asList(favourites));
        favourites=new String[favouritesList.size()-1];
        int id= getCurrentFavouriteLocationId(context);
        int i=0;
        for (Iterator<String> iter = favouritesList.listIterator(); iter.hasNext(); ) {
            String locationItem = iter.next();
            if (i==id) {
                Log.d("favourites", "location removed from favourites: "+locationItem);
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
        UsefulFunctions.setfloatingActionButtonOnClickIndicator(activity,1);
        UsefulFunctions.checkNavigationDrawerMenuItem(activity,1);
    }

    private static int getCurrentFavouriteLocationId(Context context){
        String favouritesCoordinates[]= SharedPreferencesModifier.getFavouriteLocationsCoordinates(context);
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

    public static boolean isAddressEqual(Context context){
        String favouritesAddresses[]= SharedPreferencesModifier.getFavouriteLocationsAddresses(context);
        String currentLocationHeaderString=UsefulFunctions.getCurrentLocationAddress()[0];
        String currentLocationSubheaderString=UsefulFunctions.getCurrentLocationAddress()[1];
        String currentLocationAddress=currentLocationHeaderString+", "+currentLocationSubheaderString;
        boolean isEqual=false;
        for(int i=0;i<favouritesAddresses.length;i++){
            if(favouritesAddresses[i].equals(currentLocationAddress)) isEqual=true;
        }
        return isEqual;
    }

    public static boolean isDefeaultLocationEqual(Context context, String locationAddress){
        if(locationAddress==null){
            String currentLocationHeaderString=UsefulFunctions.getCurrentLocationAddress()[0];
            String currentLocationSubheaderString=UsefulFunctions.getCurrentLocationAddress()[1];
            locationAddress=currentLocationHeaderString+", "+currentLocationSubheaderString;
        }
        boolean isEqual=false;
        String defeaultLocation=SharedPreferencesModifier.getDefeaultLocation(context);
        if(defeaultLocation!=null){
            if(defeaultLocation.equals(locationAddress)) {
                isEqual= true;
            }
        }
        return isEqual;
    }

    public static int getDefeaultLocationId(Context context){
        String[] addresses=SharedPreferencesModifier.getFavouriteLocationsAddresses(context);
        int id=-1;
        int size=addresses.length;
        for(int i=0;i<size;i++){
            if(isDefeaultLocationEqual(context,addresses[i]))id=i;
        }
        return id;
    }
}