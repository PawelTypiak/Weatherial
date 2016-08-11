package paweltypiak.matweather.weatherDataDownloading;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import paweltypiak.matweather.jsonHandling.Channel;

public class WeatherDataDownloader {

    private WeatherDownloadCallback callback;
    private String location;
    private  Exception error;
    public WeatherDataDownloader(String location, WeatherDownloadCallback callback){
        this.callback=callback;
        refreshWeather(location);
    }

    private void refreshWeather(String location){
        //download all the information from yahoo
        this.location=location;
        new AsyncTask<String, Void, String>(){
            @Override
            protected String doInBackground(String ... strings){
                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='f'",strings[0]);
                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
                Log.d("endpoint", endpoint);
                try {
                    URL url = new URL(endpoint);
                    URLConnection connection=url.openConnection();
                    InputStream inputStream=connection.getInputStream();
                    BufferedReader reader =new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result= new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        result.append(line);
                    }
                    Log.d("result", result.toString());
                    return result.toString();
                }catch(Exception e) {
                    error =e;
                }
                return null;
            }
            @Override
            protected  void onPostExecute(String s){
                if(s==null && error!=null){
                    Log.d("blad", "net ");
                    callback.ServiceFailure(1);
                    return;
                }
                try{
                    JSONObject data=new JSONObject(s);
                    JSONObject queryResluts=data.getJSONObject("query");
                    int count =queryResluts.optInt("count");
                    if (count == 0) {
                        callback.ServiceFailure(2);
                        Log.d("blad", "service ");
                        return;
                    }
                    Channel channel = new Channel();
                    channel.populate(queryResluts.optJSONObject("results").optJSONObject("channel"));
                    callback.ServiceSuccess(channel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(location);
    }
}
