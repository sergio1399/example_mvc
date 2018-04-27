package app.components.service;

import app.components.dao.ForecastDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Service
public class SimpleMessageListener implements MessageListener {

    @Autowired
    private ForecastDAO forecastDAO;

    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println("Kuku! " + textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
