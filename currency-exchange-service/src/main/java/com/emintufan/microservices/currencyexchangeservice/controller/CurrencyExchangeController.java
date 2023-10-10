package com.emintufan.microservices.currencyexchangeservice.controller;

import com.emintufan.microservices.currencyexchangeservice.dao.CurrencyExchangeRepository;
import com.emintufan.microservices.currencyexchangeservice.entity.CurrencyExchange;
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {

    private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);
    private CurrencyExchangeRepository repository;
    private Environment environment;

    @Autowired
    public CurrencyExchangeController(CurrencyExchangeRepository repository, Environment environment) {
        this.repository = repository;
        this.environment = environment;
    }

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveCurrencyEXchange(@PathVariable String from,
                                                     @PathVariable String to) {
        logger.info("retrieveExchangeValue called with {} to {}", from, to);
        CurrencyExchange exchange = repository.findByFromAndTo(from, to);
        if (exchange == null)
            throw new RuntimeException("Unable to find data for " + from + " to " + to);
        String port = environment.getProperty("local.server.port");
        exchange.setEnvironment(port);
        return new CurrencyExchange(1000L, from, to, exchange.getConversionMultiple(), port);
    }
}
