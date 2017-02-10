package paweltypiak.weatherial.dataDownloading.jsonHandling.weatherServiceTagTreeInitializing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Forecast {

    private int[] code = new int[5];
    private String high[] = new String[5];
    private String low[] = new String[5];

    public int[] getCode() {
        return code;
    }

    public String[] getHigh() {
        return high;
    }

    public String[] getLow() {
        return low;
    }

    public void forecast(JSONArray jsonArray){
        try {
            for (int i = 0; i < 5; i++) {
                JSONObject temp=jsonArray.getJSONObject(i);
                code[i]=temp.optInt("code");
                high[i]=temp.optString("high");
                low[i]=temp.optString("low");
            }
        } catch (JSONException e) {e.printStackTrace();}
    }
}
