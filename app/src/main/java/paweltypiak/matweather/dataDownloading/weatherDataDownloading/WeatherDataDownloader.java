package paweltypiak.matweather.dataDownloading.weatherDataDownloading;

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
    private  Exception error;

    public WeatherDataDownloader(String location, WeatherDownloadCallback callback){
        this.callback=callback;
        refreshWeather(location);
    }

    //// TODO: split to smaller functions
    private void refreshWeather(String locationAddress){
        new AsyncTask<String, Void, String>(){
            @Override
            protected String doInBackground(String ... addresses){
                //get weather information for location
                String endpoint=getEndpoint(addresses);
                /*Log.d("weather_downloading", "endpoint: "+endpoint);
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
                    Log.d("weather_downloading", "result: "+result.toString());
                    return result.toString();
                }catch(Exception e) {
                    error =e;
                }
                return null;*/
                String result=getResult(endpoint);
                return result;
            }
            @Override
            protected  void onPostExecute(String result){
                if(result==null && error!=null){
                    Log.d("weather_downloading", "error: internet");
                    returnWeatherServiceFailure(0);
                    return;
                }
                populateData(result);
            }
        }.execute(locationAddress);
    }

    private String getEndpoint(String... addresses){
        String YQL = String.format(
                "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='f'",
                addresses[0]
        );
        String endpoint = String.format(
                "https://query.yahooapis.com/v1/public/yql?q=%s&format=json",
                Uri.encode(YQL)
        );
        return endpoint;
    }

    private String getResult(String endpoint){
        String resultString;
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
            Log.d("weather_downloading", "result: "+result.toString());
            resultString=result.toString();
            return resultString;
        }catch(Exception e) {
            error =e;
            return null;
        }
    }



    private void populateData(String result){
        try{
            JSONObject data=new JSONObject(result);
            JSONObject queryResluts=data.getJSONObject("query");
            int count =queryResluts.optInt("count");
            if (count == 0) {
                returnWeatherServiceFailure(1);
                Log.d("weather_downloading", "error: service");
                return;
            }
            Channel channel = new Channel();
            channel.populate(queryResluts.optJSONObject("results").optJSONObject("channel"));
            Log.d("weather_downloading", "service success");
            returnWeatherServiceSuccess(channel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void returnWeatherServiceFailure(int code){
        callback.weatherServiceFailure(code);
    }

    private void returnWeatherServiceSuccess(Channel channel){
        callback.weatherServiceSuccess(channel);
    }
}
