package paweltypiak.matweather.weatherDataDownloading;

import paweltypiak.matweather.jsonHandling.Channel;


public interface WeatherDownloadCallback {
    void ServiceSuccess(Channel channel);
    void ServiceFailure(int errorCode);
}
