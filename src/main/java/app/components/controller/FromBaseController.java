package app.components.controller;

import app.components.service.ForecastService;
import app.components.view.ForecastCityView;
import app.components.view.ResponseView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "find", produces = APPLICATION_JSON_VALUE)
public class FromBaseController {

    @Autowired
    ForecastService service;

    @RequestMapping(value = "/view_from_base", method = {GET})
    public ForecastCityView getFromBase(@RequestParam(value="city", required=true) String city){
        ForecastCityView view = service.getForecast(city);

        return view;
    }
}
