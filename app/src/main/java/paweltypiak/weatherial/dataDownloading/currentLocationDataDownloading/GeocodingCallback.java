package paweltypiak.weatherial.dataDownloading.currentLocationDataDownloading;

public interface GeocodingCallback {

    void geocodingServiceSuccess(String location);

    void geocodingServiceFailure(int errorCode);
}
