package app.components.utils;

import app.components.model.City;
import app.components.model.Forecast;
import app.components.view.ForecastCityView;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ForecastConverter {

    public static Forecast jsonToForecast(JSONObject json){
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
        }
        return forecast;
    }

    public static ForecastCityView jsonToForecastCityView(JSONObject json){
        JSONObject location = json.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("location");
        String cityName = location.getString("city");
        String cityCountry = location.getString("country");
        String cityRegion = location.getString("region");
        String link = json.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getString("link");
        String cityId = link.split("-")[1].split("/")[0];
        ForecastCityView view = new ForecastCityView(jsonToForecast(json), Integer.valueOf(cityId), cityName, cityRegion, cityCountry);
        return view;
    }

    public static Forecast viewToForecast(ForecastCityView view){
        Forecast forecast = new Forecast(view.temperature, view.wind, view.text, view.pressure, view.visibility, view.forecastDate);
        forecast.setCity(viewToCity(view));
        return forecast;
    }

    public static City viewToCity(ForecastCityView view){
        return new City(view.cityId, view.cityName, view.cityRegion, view.cityCountry);
    }

}
