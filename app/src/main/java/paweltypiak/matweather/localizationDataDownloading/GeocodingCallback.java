package paweltypiak.matweather.localizationDataDownloading;

import paweltypiak.matweather.jsonHandling.Geocoding;

public interface GeocodingCallback {
    void geocodeSuccess(String location);

    void geocodeFailure(int errorCode);
}
