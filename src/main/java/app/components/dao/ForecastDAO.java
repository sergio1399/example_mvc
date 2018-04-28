package app.components.dao;

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
    public boolean save(Forecast forecast)
    {
        entityManager.persist(forecast);
        return true;
    }
}
