package paweltypiak.matweather.jsonHandling;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Forecast {

    JSONArray jsonArray;
    private int[] code = new int[5];
    private int high[] = new int[5];
    private int low[] = new int[5];

    public int[] getCode() {
        return code;
    }
    public int[] getHigh() {
        return high;
    }
    public int[] getLow() {
        return low;
    }

    public void forecast(JSONArray f){
        jsonArray = f;
        try {
            for (int i = 0; i < 5; i++) {
                JSONObject temp=jsonArray.getJSONObject(i);
                code[i]=temp.optInt("code");
                high[i]=temp.optInt("high");
                low[i]=temp.optInt("low");
            }
        } catch (JSONException e) {e.printStackTrace();}
    }
}
