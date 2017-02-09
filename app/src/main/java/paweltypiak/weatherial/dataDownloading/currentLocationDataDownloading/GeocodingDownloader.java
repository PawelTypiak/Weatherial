package paweltypiak.weatherial.dataDownloading.currentLocationDataDownloading;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.jsonHandling.Geocoding;

public class GeocodingDownloader {

    private GeocodingCallback geocodingCallback;
    private Exception error;

    public GeocodingDownloader(Activity activity,
                               Location location,
                               GeocodingCallback geocodingCallback,
                               TextView textView
                               ) {
        this.geocodingCallback = geocodingCallback;
        textView.setText(activity.getString(R.string.looking_for_address_progress_message));
        startGeocoding(location);
    }

    private void startGeocoding(Location location) {
        new AsyncTask<Location, Void, String>() {
            @Override
            protected String doInBackground(Location... locations) {
                //getting geocoding for current location coordinates
                StringBuilder result=getResult(locations);
                String currentLocationAddress=populateData(result);
                return currentLocationAddress;
            }
            @Override
            protected void onPostExecute(String currentLocationAddress) {
                try{
                    if (currentLocationAddress == null && error != null) {
                        Log.d("geocoding", "service failure");
                        geocodingCallback.geocodingServiceFailure(getErrorCode(error));
                    } else {
                        Log.d("geocoding", "service success");
                        geocodingCallback.geocodingServiceSuccess(currentLocationAddress);
                    }
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        }.execute(location);
    }

    private StringBuilder getResult(Location... locations){
        StringBuilder result = new StringBuilder();
        try{
            Location location = locations[0];
            String endpoint
                    = String.format(
                    "https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&key=%s",
                    location.getLatitude(),
                    location.getLongitude(),
                    "");
            Log.d("geocoding", "endpoint: "+endpoint);
            URL url = new URL(endpoint);
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            InputStream inputStream;
            inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result;
        }catch(Exception e) {
            Log.d("geocoding", "internet error");
            error = new GeocoderException("0");
            return null;
        }
    }

    private String populateData(StringBuilder result){
        JSONArray results;
        try{
            JSONObject data = new JSONObject(result.toString());
            results = data.optJSONArray("results");
        }catch (Exception e){
            error = new GeocoderException("1");
            return null;
        }
        if (results.length() == 0) {
            Log.d("geocoding", "geocoding error");
            error = new GeocoderException("1");
            return null;
        }
        Geocoding geocoding = new Geocoding();
        geocoding.populate(results.optJSONObject(0));
        return geocoding.getAddress();
    }

    private int getErrorCode(Exception error){
        //get error code from exception message
        String errorString=error.toString();
        errorString=errorString.substring(errorString.length()-1);
        if(errorString.equals("0")) {
            return Integer.parseInt(errorString);
        }
        else {
            return 1;
        }
    }

    private class GeocoderException extends Exception {
        //custom exception message
        private GeocoderException(String errorCode) {
            super(errorCode);
        }
    }
}
