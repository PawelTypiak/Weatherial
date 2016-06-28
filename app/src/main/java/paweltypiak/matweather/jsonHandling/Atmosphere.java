package paweltypiak.matweather.jsonHandling;

import org.json.JSONObject;

public class Atmosphere implements JSONPopulator {


    private int humidity;
    private String pressure;
    private String visibility;

    public int getHumidity() {
        return humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public String getVisibility() {
        return visibility;
    }

    @Override
    public void populate(JSONObject data) {
        humidity=data.optInt("humidity");
        pressure=data.optString("pressure");
        visibility=data.optString("visibility");
    }
}
