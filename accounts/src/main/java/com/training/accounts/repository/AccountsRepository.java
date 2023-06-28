package com.training.accounts.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.training.accounts.model.Accounts;

@Repository
public interface AccountsRepository extends MongoRepository<Accounts, String> {

	Accounts findByCustomerId(int customerId);

}
