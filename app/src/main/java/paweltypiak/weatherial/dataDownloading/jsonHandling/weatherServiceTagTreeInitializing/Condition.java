package paweltypiak.weatherial.dataDownloading.jsonHandling.weatherServiceTagTreeInitializing;

import org.json.JSONObject;

import paweltypiak.weatherial.dataDownloading.jsonHandling.JSONPopulator;

public class Condition implements JSONPopulator {

    private int code;
    private String temperature;

    public int getCode() {
        return code;
    }

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        code=data.optInt("code");
        temperature=data.optString("temp");
    }
}