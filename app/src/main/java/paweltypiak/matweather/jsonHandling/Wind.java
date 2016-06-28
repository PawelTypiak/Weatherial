package paweltypiak.matweather.jsonHandling;

import org.json.JSONObject;

public class Wind implements JSONPopulator{

    private int chill;
    private String direction;
    private int speed;

    public int getChill() {
        return chill;
    }

    public String getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public void populate(JSONObject data) {
        chill=data.optInt("chill");
        direction=data.optString("direction");
        speed=data.optInt("speed");
    }
}
