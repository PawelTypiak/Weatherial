package paweltypiak.matweather.localizationDataDownloading;

import paweltypiak.matweather.jsonHandling.Geocoding;

public interface GeocodingCallback {
    void geocodeSuccess(Geocoding location);

    void geocodeFailure(Exception exception);
}
