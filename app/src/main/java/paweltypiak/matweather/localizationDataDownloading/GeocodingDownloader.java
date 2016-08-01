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
import paweltypiak.matweather.UsefulFunctions;
import paweltypiak.matweather.jsonHandling.Geocoding;

public class GeocodingDownloader {
    private GeocodingCallback geocodingCallback;
    private Exception error;
    private Location location;
    private Geocoding geocoding = new Geocoding();
    private TextView textView;
    public GeocodingDownloader(Location location, GeocodingCallback geocodingCallback, TextView textView, Activity activity) {
        this.geocodingCallback = geocodingCallback;
        textView.setText(activity.getString(R.string.first_launch_layout_loading_header_geocoding));

        refreshLocation(location);
    }

    public void refreshLocation(Location location) {
        new AsyncTask<Location, Void, String>() {
            @Override
            protected String doInBackground(Location... locations) {
                Location location = locations[0];
                String endpoint = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&key=%s", location.getLatitude(), location.getLongitude(), "");
                Log.d("geoendpoint", endpoint);
                try {
                    InputStream inputStream;
                    try{
                        URL url = new URL(endpoint);
                        URLConnection connection = url.openConnection();
                        connection.setUseCaches(false);
                        inputStream = connection.getInputStream();
                    }catch(Exception e) {
                        Log.d("error neta", "error ");
                        error = new GeocoderException("1");
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
                        error = new GeocoderException("2");
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
                //Log.d("location", "locaton: "+location.getAddress());
                try{
                    if (location == null && error != null) {
                        Log.d("location", "nie wyszlo ");
                        geocodingCallback.geocodeFailure(getErrorCode(error));
                    } else {
                        Log.d("location", "sukcess ");
                        geocodingCallback.geocodeSuccess(location);
                    }
                }catch (Exception exception){}
            }
        }.execute(location);
    }

    private int getErrorCode(Exception error){
        String errorString=error.toString();
        errorString=errorString.substring(errorString.length()-1);
        Log.d("location", errorString);
        if(errorString.equals("1")) return Integer.parseInt(errorString);
        else return 2;
    }
    private class GeocoderException extends Exception {
        public GeocoderException(String errorCode) {
            super(errorCode);
        }
    }
}
