package paweltypiak.matweather.weatherDataDownloading;

import paweltypiak.matweather.jsonHandling.Channel;


public interface WeatherDownloadCallback {
    void weatherServiceSuccess(Channel channel);
    void weatherServiceFailure(int errorCode);
}
