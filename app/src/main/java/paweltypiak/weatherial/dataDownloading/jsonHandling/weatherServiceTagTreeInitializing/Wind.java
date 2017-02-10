package paweltypiak.weatherial.dataDownloading.jsonHandling.weatherServiceTagTreeInitializing;

import org.json.JSONObject;

import paweltypiak.weatherial.dataDownloading.jsonHandling.JSONPopulator;

public class Wind implements JSONPopulator {

    private String direction;
    private String speed;

    public String getDirection() {
        return direction;
    }

    public String getSpeed() {
        return speed;
    }

    @Override
    public void populate(JSONObject data) {
        direction=data.optString("direction");
        speed=data.optString("speed");
    }
}
