package paweltypiak.weatherial.jsonHandling;

import org.json.JSONObject;

public class Location implements JSONPopulator{

    private String city;
    private String country;
    private String region;

    public String getCity() {return city;}

    public String getCountry() {return country;}

    public String getRegion() {return region;}

    @Override
    public void populate(JSONObject data){
        city=data.optString("city");
        country=data.optString("country");
        region=data.optString("region");
    }
}
