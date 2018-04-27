package app.components.model;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "Forecast")
public class Forecast {

    @Id
    @GeneratedValue
    private  Integer id;

    private String temperature;

    private String wind;

    private String text;

    private Double pressure;

    private Double visibility;

    @Column(name = "city_id")
    private Integer cityId;

    @Temporal(TemporalType.DATE)
    private Date forecastDate;

    public Date getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(Date forecastDate) {
        this.forecastDate = forecastDate;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getVisibility() {
        return visibility;
    }

    public void setVisibility(Double visibility) {
        this.visibility = visibility;
    }

    /* Constructors */

    public Forecast() {
    }

    public Forecast(String temperature, String wind, String text, Integer cityId) {
        this.temperature = temperature;
        this.wind = wind;
        this.text = text;
        this.cityId = cityId;
    }

    public Forecast(String temperature, String wind, String text) {
        this.temperature = temperature;
        this.wind = wind;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "temperature='" + temperature + '\'' +
                ", wind='" + wind + '\'' +
                ", text='" + text + '\'' +
                ", pressure=" + pressure +
                ", visibility=" + visibility +
                ", cityId=" + cityId +
                '}';
    }
}

