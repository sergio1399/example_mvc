package app.components.view;

import app.components.model.City;
import app.components.model.Forecast;

import java.util.Date;

public class ForecastCityView {
    public String cityName;

    public String cityRegion;

    public String cityCountry;

    public String temperature;

    public String wind;

    public String text;

    public Double pressure;

    public Double visibility;

    public Integer cityId;

    public Date forecastDate;

    public ForecastCityView() {
    }

    public ForecastCityView(String cityName, String cityRegion, String cityCountry, String temperature, String wind, String text, Double pressure, Double visibility, Integer cityId, Date forecastDate) {
        this.cityName = cityName;
        this.cityRegion = cityRegion;
        this.cityCountry = cityCountry;
        this.temperature = temperature;
        this.wind = wind;
        this.text = text;
        this.pressure = pressure;
        this.visibility = visibility;
        this.cityId = cityId;
        this.forecastDate = forecastDate;
    }

    public ForecastCityView(Forecast forecast,Integer cityId,  String cityName, String cityRegion, String cityCountry){
        this.cityId = cityId;
        this.cityName = cityName;
        this.cityRegion = cityRegion;
        this.cityCountry = cityCountry;
        this.temperature = forecast.getTemperature();
        this.wind = forecast.getWind();
        this.text = forecast.getText();
        this.pressure = forecast.getPressure();
        this.visibility = forecast.getVisibility();
        this.forecastDate = forecast.getForecastDate();
    }

    public ForecastCityView(Forecast forecast, City city){
        this.cityId = city.getId();
        this.cityName = city.getName();
        this.cityRegion = city.getRegion();
        this.cityCountry = city.getCountry();
        this.temperature = forecast.getTemperature();
        this.wind = forecast.getWind();
        this.text = forecast.getText();
        this.pressure = forecast.getPressure();
        this.visibility = forecast.getVisibility();
        this.forecastDate = forecast.getForecastDate();
    }

    @Override
    public String toString() {
        return "ForecastCityView{" +
                "temperature='" + temperature + '\'' +
                ", wind='" + wind + '\'' +
                ", text='" + text + '\'' +
                ", pressure=" + pressure +
                ", visibility=" + visibility +
                ", cityId=" + cityId +
                '}';
    }
}
