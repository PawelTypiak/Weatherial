package paweltypiak.matweather.localizationDataDownloading;

public interface GeocodingCallback {

    void geocodingServiceSuccess(String location);

    void geocodingServiceFailure(int errorCode);
}
