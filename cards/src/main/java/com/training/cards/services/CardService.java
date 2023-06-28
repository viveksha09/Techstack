package com.training.cards.services;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.cards.model.Cards;
import com.training.cards.repository.CardsRepository;

@Service
public class CardService {
	@Autowired
	private CardsRepository cardsRepository;

	// Perform CRUD operation

	public Cards addCardInfo(Cards cardInfo) {
		cardInfo.setCardId(new Random().nextInt());
		return cardsRepository.save(cardInfo);
	}

	public List<Cards> findAllCardInfo() {
		return cardsRepository.findAll();
	}

	public Cards getCardDetailsByCardId(int cardId) {
		return cardsRepository.findById((long) cardId).get();
	}

	public Cards updateAccount(Cards cardInfo) {
		// get existing document from DB.populate new value from request to existing
		// document

		Cards existingTask = cardsRepository.findById((long) cardInfo.getCardId()).get();
		existingTask.setAmountUsed(cardInfo.getAmountUsed());
		existingTask.setAvailableAmount(cardInfo.getAvailableAmount());
		existingTask.setCustomerId(cardInfo.getCustomerId());
		existingTask.setCardNumber(cardInfo.getCardNumber());
		existingTask.setCardType(cardInfo.getCardType());
		existingTask.setTotalLimit(cardInfo.getTotalLimit());
		existingTask.setCreateDt(cardInfo.getCreateDt());

		return cardsRepository.save(existingTask);
	}

	public String deleteAccount(int cardId) {
		cardsRepository.deleteById((long) cardId);
		return cardId + "task deleted from dashboard";
	}

}