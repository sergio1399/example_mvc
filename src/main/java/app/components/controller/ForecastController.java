package app.components.controller;

import java.util.*;

import app.components.utils.ForecastConverter;
import app.components.view.ForecastCityView;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

/**
 * @author imssbora
 */
@Controller
public class ForecastController {
    @Autowired
    private JmsTemplate jmsTemplate;

    @RequestMapping(path={"/"},method=RequestMethod.GET)
    public String init(Model model) {

        return "menu";
    }

    @RequestMapping(path={"/add"},method=RequestMethod.GET)
    public String add(Model model) {

        return "add";
    }

    @RequestMapping(path={"/find"},method=RequestMethod.GET)
    public String find(Model model) {

        return "find";
    }


    @RequestMapping(path={"/forecast"},method=RequestMethod.GET)
    public String forecast(@RequestParam(value="city", required=true) String city, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String str_uri = "https://query.yahooapis.com/v1/public/yql?q=";
        str_uri += String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")", city);

        restTemplate.setMessageConverters(getMessageConverters());
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<Object> response =
                restTemplate.exchange(str_uri, HttpMethod.GET, entity, Object.class, "1");
        Object object = response.getBody();

        JSONObject json = new JSONObject((LinkedHashMap<String, String>) object);

        //Forecast forecast = ForecastConverter.JsonToForecast(json);
        ForecastCityView forecastCityView = ForecastConverter.jsonToForecastCityView(json);

        jmsTemplate.convertAndSend(forecastCityView);

        model.addAttribute("city", city);
        model.addAttribute("forecast",forecastCityView.toString());
        return "forecast";
    }


    private List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> converters =
                new ArrayList<HttpMessageConverter<?>>();
        converters.add(new MappingJackson2HttpMessageConverter());
        return converters;
    }
}