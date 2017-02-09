package paweltypiak.weatherial.dataDownloading.weatherDataDownloading;

import paweltypiak.weatherial.jsonHandling.Channel;

public interface WeatherDownloadCallback {

    void weatherServiceSuccess(Channel channel);

    void weatherServiceFailure(int errorCode);
}
