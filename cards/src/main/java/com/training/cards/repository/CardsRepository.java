package com.training.cards.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.training.cards.model.Cards;

@Repository
public interface CardsRepository extends MongoRepository<Cards, Long> {

	List<Cards> findByCustomerId(int customerId);

}
