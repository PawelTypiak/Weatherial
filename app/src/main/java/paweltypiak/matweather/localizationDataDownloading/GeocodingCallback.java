package paweltypiak.matweather.localizationDataDownloading;

import paweltypiak.matweather.jsonHandling.Geocoding;

public interface GeocodingCallback {
    void geocodingServiceSuccess(String location);

    void geocodingServiceFailure(int errorCode);
}
