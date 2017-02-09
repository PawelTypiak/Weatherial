package paweltypiak.weatherial.jsonHandling;

import org.json.JSONObject;

public class Wind implements JSONPopulator{

    private String chill;
    private String direction;
    private String speed;

    public String getChill() {
        return chill;
    }

    public String getDirection() {
        return direction;
    }

    public String getSpeed() {
        return speed;
    }

    @Override
    public void populate(JSONObject data) {
        chill=data.optString("chill");
        direction=data.optString("direction");
        speed=data.optString("speed");
    }
}
