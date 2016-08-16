package paweltypiak.matweather.jsonHandling;

import org.json.JSONArray;
import org.json.JSONObject;

public class Item implements JSONPopulator{
    private Condition condition;
    private JSONArray jsonArray;
    private Forecast forecast;
    private double latitude;
    private double longitude;

    public Condition getCondition() {
        return condition;
    }
    public Forecast getForecast() {
        return forecast;
    }
    public double getLatitude() {return latitude; }
    public double getLongitude() {return longitude; }

    @Override
    public void populate(JSONObject data) {
        latitude=data.optDouble("lat");
        longitude=data.optDouble("long");
        condition=new Condition();
        forecast=new Forecast();
        condition.populate(data.optJSONObject(("condition")));
        jsonArray=data.optJSONArray("forecast");
        forecast.forecast(jsonArray);
    }
}