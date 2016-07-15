package paweltypiak.matweather.jsonHandling;

import org.json.JSONObject;

public class Condition implements JSONPopulator {

    private int code;
    private String temperature;
    private String description;

    public int getCode() {
        return code;
    }
    public String getTemperature() {
        return temperature;
    }
    public String getDescription() {
        return description;
    }

    @Override
    public void populate(JSONObject data) {
        code=data.optInt("code");
        temperature=data.optString("temp");
        description=data.optString("text");
    }
}