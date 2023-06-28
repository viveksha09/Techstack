/**
 * 
 */
package com.training.cards.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.training.cards.config.CardsServiceConfig;
import com.training.cards.model.Cards;
import com.training.cards.model.Properties;
import com.training.cards.repository.CardsRepository;
import com.training.cards.services.CardService;

@RestController
public class CardsController {

	@Autowired
	private CardsRepository cardsRepository;

	@Autowired
	CardService service;
	@Autowired
	CardsServiceConfig cardsConfig;

	@PostMapping("/myCards")
	public Cards createCards(@RequestBody Cards cardInfo) {
		return service.addCardInfo(cardInfo);
	}

	@GetMapping
	public List<Cards> getCardsDetails() {
		return service.findAllCardInfo();
	}

	@PutMapping
	public Cards modifyTask(@RequestBody Cards acc) {
		return service.updateAccount(acc);
	}

	@GetMapping("/cards/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties properties = new Properties(cardsConfig.getMsg(), cardsConfig.getBuildVersion(),
				cardsConfig.getMailDetails(), cardsConfig.getActiveBranches());
		String jsonStr = ow.writeValueAsString(properties);
		return jsonStr;
	}

}
