package app.components.service;

import app.components.dao.ForecastDAO;
import app.components.model.Forecast;
import app.components.utils.ForecastConverter;
import app.components.view.ForecastCityView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListenService {
    @Autowired
    private ForecastDAO dao;

    @Transactional
    public void save(ForecastCityView view){

        Forecast forecast = ForecastConverter.viewToForecast(view);
        dao.saveCityAndForecast(forecast.getCity(), forecast);
        //throw new RuntimeException("Handmade exception!");
    }

}
