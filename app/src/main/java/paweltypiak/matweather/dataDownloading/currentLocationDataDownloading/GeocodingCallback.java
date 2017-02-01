package paweltypiak.matweather.dataDownloading.currentLocationDataDownloading;

public interface GeocodingCallback {

    void geocodingServiceSuccess(String location);

    void geocodingServiceFailure(int errorCode);
}
