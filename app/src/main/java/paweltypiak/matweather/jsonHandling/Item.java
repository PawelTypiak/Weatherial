package paweltypiak.matweather.jsonHandling;

import org.json.JSONArray;
import org.json.JSONObject;

public class Item implements JSONPopulator{

    private Condition condition;
    private JSONArray jsonArray;
    private Forecast forecast;

    public Condition getCondition() {
        return condition;
    }
    public Forecast getForecast() {
        return forecast;
    }

    @Override
    public void populate(JSONObject data) {
        condition=new Condition();
        forecast=new Forecast();
        condition.populate(data.optJSONObject(("condition")));
        jsonArray=data.optJSONArray("forecast");
        forecast.forecast(jsonArray);
    }
}