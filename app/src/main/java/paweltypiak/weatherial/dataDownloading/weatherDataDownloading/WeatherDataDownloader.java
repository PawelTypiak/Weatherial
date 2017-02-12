/*******************************************************************************
 * Copyright 2017 Paweł Typiak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package paweltypiak.weatherial.dataDownloading.weatherDataDownloading;

import android.net.Uri;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import paweltypiak.weatherial.dataDownloading.jsonHandling.weatherServiceTagTreeInitializing.Channel;

public class WeatherDataDownloader {

    private WeatherDownloadCallback callback;
    private  Exception error;

    public WeatherDataDownloader(String location, WeatherDownloadCallback callback){
        this.callback=callback;
        downloadWeatherData(location);
    }

    private void downloadWeatherData(String locationAddress){
        new AsyncTask<String, Void, String>(){
            @Override
            protected String doInBackground(String ... addresses){
                //get weather information for location
                String endpoint=getEndpoint(addresses);
                String result=getResult(endpoint);
                return result;
            }
            @Override
            protected  void onPostExecute(String result){
                if(result==null && error!=null){
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
            JSONObject queryResults=data.getJSONObject("query");
            int count =queryResults.optInt("count");
            if (count == 0) {
                returnWeatherServiceFailure(1);
                return;
            }
            Channel channel = new Channel();
            channel.populate(queryResults.optJSONObject("results").optJSONObject("channel"));
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
