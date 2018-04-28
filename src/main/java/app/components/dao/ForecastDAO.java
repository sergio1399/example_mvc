package app.components.dao;

import app.components.model.City;
import app.components.model.Forecast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ForecastDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public boolean saveCityAndForecast(City city, Forecast forecast){
        City oldCity = entityManager.find(City.class, city.getId());
        if(oldCity == null) {
            entityManager.persist(city);
        }
        city = entityManager.find(City.class, city.getId());
        forecast.setCity(city);
        entityManager.persist(forecast);
        return true;
    }
}
