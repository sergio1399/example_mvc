package app.components.service;

import app.components.dao.ForecastDAO;
import app.components.model.City;
import app.components.model.Forecast;
import app.components.utils.ForecastConverter;
import app.components.view.ForecastCityView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForecastService {
    @Autowired
    private ForecastDAO dao;

    public ForecastCityView getForecast(String name){
        City city = dao.getCity(name);
        Forecast forecast = dao.getForecast(name);
        ForecastCityView view = ForecastConverter.toView(city, forecast);
        return view;
    }
}
