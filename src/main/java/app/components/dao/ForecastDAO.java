package app.components.dao;

import app.components.model.Forecast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ForecastDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean save(Forecast forecast)
    {

        return false;
    }
}
