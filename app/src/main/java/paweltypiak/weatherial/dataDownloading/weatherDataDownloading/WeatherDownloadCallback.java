package paweltypiak.weatherial.dataDownloading.weatherDataDownloading;

import paweltypiak.weatherial.dataDownloading.jsonHandling.weatherServiceTagTreeInitializing.Channel;

public interface WeatherDownloadCallback {

    void weatherServiceSuccess(Channel channel);

    void weatherServiceFailure(int errorCode);
}
