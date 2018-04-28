package app.components.utils;

import app.components.model.Forecast;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ForecastConverter {

    public static Forecast JsonToForecast(JSONObject json){
        JSONObject channel = json.getJSONObject("query").getJSONObject("results").getJSONObject("channel");
        Forecast forecast = new Forecast();
        if(channel.has("wind")) {
            JSONObject wind = channel.getJSONObject("wind");
            forecast.setWind("chill: " +  String.valueOf(wind.getInt("chill")) + ", direction:" + String.valueOf(wind.getInt("direction")) + ", speed:" + String.valueOf(wind.getInt("speed")) );
        }
        if(channel.has("atmosphere")) {
            JSONObject atmo = channel.getJSONObject("atmosphere");
            forecast.setPressure(atmo.getDouble("pressure"));
            forecast.setVisibility(atmo.getDouble("visibility"));
        }
        if(channel.has("item")) {
            JSONObject condition = channel.getJSONObject("item").getJSONObject("condition");
            SimpleDateFormat format = new SimpleDateFormat();
            format.applyPattern("dd.MM.yyyy");
            try {
                forecast.setForecastDate(format.parse(condition.getString("date")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            forecast.setTemperature(condition.getString("temp"));
            forecast.setText(condition.getString("text"));
            String link = channel.getString("link");
            String cityId = link.split("-")[1].split("/")[0];
            forecast.setCityId(Integer.valueOf(cityId));
        }
        return forecast;
    }

}
