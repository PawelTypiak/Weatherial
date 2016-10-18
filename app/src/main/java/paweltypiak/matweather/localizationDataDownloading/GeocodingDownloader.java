package paweltypiak.matweather.localizationDataDownloading;

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
import paweltypiak.matweather.R;
import paweltypiak.matweather.jsonHandling.Geocoding;

public class GeocodingDownloader {

    private GeocodingCallback geocodingCallback;
    private Exception error;
    private Geocoding geocoding = new Geocoding();

    public GeocodingDownloader(Location location, GeocodingCallback geocodingCallback, TextView textView, Activity activity) {
        this.geocodingCallback = geocodingCallback;
        textView.setText(activity.getString(R.string.looking_for_address_progress_message));
        refreshLocation(location);
    }

    public void refreshLocation(Location location) {
        new AsyncTask<Location, Void, String>() {
            @Override
            protected String doInBackground(Location... locations) {
                //getting geocoding for current location coordinates
                Location location = locations[0];
                String endpoint = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&key=%s", location.getLatitude(), location.getLongitude(), "");
                Log.d("geocoding", "endpoint: "+endpoint);
                try {
                    InputStream inputStream;
                    try{
                        URL url = new URL(endpoint);
                        URLConnection connection = url.openConnection();
                        connection.setUseCaches(false);
                        inputStream = connection.getInputStream();
                    }catch(Exception e) {
                        Log.d("geocoding", "internet error");
                        error = new GeocoderException("0");
                        return null;
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    JSONObject data = new JSONObject(result.toString());
                    JSONArray results = data.optJSONArray("results");
                    if (results.length() == 0) {
                        Log.d("geocoding", "geocoding error");
                        error = new GeocoderException("1");
                        return null;
                    }
                    geocoding.populate(results.optJSONObject(0));
                    return geocoding.getAddress();
                } catch (Exception e) {
                    error = e;
                }
                return null;
            }
            @Override
            protected void onPostExecute(String location) {
                try{
                    if (location == null && error != null) {
                        Log.d("geocoding", "service failure");
                        geocodingCallback.geocodingServiceFailure(getErrorCode(error));
                    } else {
                        Log.d("geocoding", "service success");
                        geocodingCallback.geocodingServiceSuccess(location);
                    }
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        }.execute(location);
    }

    private int getErrorCode(Exception error){
        //get error code from exception message
        String errorString=error.toString();
        errorString=errorString.substring(errorString.length()-1);
        if(errorString.equals("0")) return Integer.parseInt(errorString);
        else return 1;
    }

    private class GeocoderException extends Exception {
        //custom exception message
        public GeocoderException(String errorCode) {
            super(errorCode);
        }
    }
}
