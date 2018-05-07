package app.components.service;

import app.components.dao.ForecastDAO;
import app.components.model.City;
import app.components.model.Forecast;
import app.components.utils.ForecastConverter;
import app.components.view.ForecastCityView;
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
    private ListenService service;

    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        ObjectMapper objectMapper = new ObjectMapper();
        ForecastCityView view = null;
        try {
            view = objectMapper.readValue(textMessage.getText(), ForecastCityView.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        service.save(view);
    }
}
