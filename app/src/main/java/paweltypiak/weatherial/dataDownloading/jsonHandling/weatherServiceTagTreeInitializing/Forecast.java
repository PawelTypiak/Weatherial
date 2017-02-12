/*******************************************************************************
 * Copyright 2017 Paweł Typiak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
