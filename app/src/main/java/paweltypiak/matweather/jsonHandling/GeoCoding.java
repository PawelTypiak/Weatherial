package paweltypiak.matweather.jsonHandling;

import org.json.JSONObject;

public class Geocoding implements JSONPopulator{

        private String address;

        public String getAddress() {
            return address;
        }

        @Override
        public void populate(JSONObject data) {
            address = data.optString("formatted_address");
        }
}
