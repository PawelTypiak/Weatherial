package paweltypiak.matweather.localizationDataDownloading;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import paweltypiak.matweather.jsonHandling.Geocoding;

/**
 * Created by Pawcioch on 30.07.2016.
 */
public class GeocodingDownloader {
    private GeocodingCallback geocodingCallback;
    private Exception error;
    private Location location;
    private Geocoding geocoding = new Geocoding();
    public GeocodingDownloader(Location location,GeocodingCallback geocodingCallback) {
        this.geocodingCallback = geocodingCallback;
        this.location=location;
        refreshLocation(location);
    }

    public void refreshLocation(Location location) {
        new AsyncTask<Location, Void, Geocoding>() {
            @Override
            protected Geocoding doInBackground(Location... locations) {

                Location location = locations[0];
                Log.d("reflocat", "jestem 1 ");
                String endpoint = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&key=%s", location.getLatitude(), location.getLongitude(), API_KEY);

                try {
                    InputStream inputStream=null;
                    try{
                        URL url = new URL(endpoint);

                        URLConnection connection = url.openConnection();

                        connection.setUseCaches(false);
                        inputStream = connection.getInputStream();
                    }catch(Exception e) {
                        Log.d("error neta", "error ");
                        error = new GeocoderException("1");
                    }




                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    Log.d("reflocat", "jestem 2 ");
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    JSONObject data = new JSONObject(result.toString());

                    JSONArray results = data.optJSONArray("results");

                    if (results.length() == 0) {
                        error = new GeocoderException("2");

                        return null;
                    }
                    Log.d("reflocat", "jestem 3 ");

                    geocoding.populate(results.optJSONObject(0));
                    return geocoding;


                } catch (Exception e) {
                    error = e;
                }

                return null;

            }

            @Override
            protected void onPostExecute(Geocoding location) {
                Log.d("location", "locaton: "+location.getAddress());
                Log.d("location", "error: "+error);
                if (location == null && error != null) {
                    Log.d("location", "nie wyszlo ");
                    geocodingCallback.geocodeFailure(error);
                } else {
                    Log.d("location", "sukcess ");
                    geocodingCallback.geocodeSuccess(location);
                }

            }

        }.execute(location);
    }

    // OPTIONAL: Your Google Maps API KEY
    private static final String API_KEY = "";

    private class GeocoderException extends Exception {
        public GeocoderException(String errorCode) {
            super(errorCode);
        }
    }

}
