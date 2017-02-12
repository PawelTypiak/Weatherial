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