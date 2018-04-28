package app.components.service;

import app.components.dao.ForecastDAO;
import app.components.model.Forecast;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;

@Service
public class SimpleMessageListener implements MessageListener {

    @Autowired
    private ForecastDAO forecastDAO;

    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        ObjectMapper objectMapper = new ObjectMapper();
        Forecast forecast = null;
        try {
            forecast = objectMapper.readValue(textMessage.getText(), Forecast.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        forecastDAO.save(forecast);
    }
}
