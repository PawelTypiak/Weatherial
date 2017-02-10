package paweltypiak.weatherial.dataDownloading.jsonHandling.weatherServiceTagTreeInitializing;

import org.json.JSONObject;

import paweltypiak.weatherial.dataDownloading.jsonHandling.JSONPopulator;

public class Channel implements JSONPopulator {

    private Item item;
    private Astronomy astronomy;
    private Wind wind;
    private Atmosphere atmosphere;
    private String link;
    private Location location;
    private String lastBuildDate;

    public String getLastBuildDate() {return lastBuildDate;}

    public Item getItem() {
        return item;
    }

    public String getLink() {return link;}

    public Astronomy getAstronomy() {
        return astronomy;
    }

    public Wind getWind() {
        return wind;
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    public Location getLocation() {return location;}

    @Override
    public void populate(JSONObject data) {
        link=data.optString("link");
        location=new Location();
        location.populate(data.optJSONObject("location"));
        lastBuildDate=data.optString("lastBuildDate");
        item=new Item();
        item.populate(data.optJSONObject("item"));
        astronomy=new Astronomy();
        astronomy.populate(data.optJSONObject("astronomy"));
        wind=new Wind();
        wind.populate(data.optJSONObject("wind"));
        atmosphere=new Atmosphere();
        atmosphere.populate(data.optJSONObject("atmosphere"));
    }
}