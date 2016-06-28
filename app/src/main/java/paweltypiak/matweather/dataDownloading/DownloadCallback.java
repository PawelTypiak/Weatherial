package paweltypiak.matweather.dataDownloading;

import paweltypiak.matweather.jsonHandling.Channel;


public interface DownloadCallback {
    void ServiceSuccess(Channel channel);
    void ServiceFailure(Exception exception);
}
