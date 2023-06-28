package com.training.accounts.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.accounts.model.Accounts;
import com.training.accounts.repository.AccountsRepository;

@Service
public class AccountService {

	@Autowired
	private AccountsRepository accountRepository;

	// Perform CRUD operation
	public Accounts addAccount(Accounts account) {
		account.setAccountNumber(UUID.randomUUID().toString().split("-")[0]);

		return accountRepository.save(account);
	}

	public List<Accounts> findAllAccounts() {
		return accountRepository.findAll();
	}

	public Accounts getAccountByAccountNo(String accountNumber) {
		return accountRepository.findById(accountNumber).get();
	}

	public Accounts updateAccount(Accounts taskRequest) {
		// get existing document from DB.populate new value from request to existing
		// document

		Accounts existingTask = accountRepository.findById(taskRequest.getAccountNumber()).get();
		existingTask.setAccountType(taskRequest.getAccountType());
		existingTask.setBranchAddress(taskRequest.getBranchAddress());
		existingTask.setCustomerId(taskRequest.getCustomerId());
		existingTask.setCreateDt(taskRequest.getCreateDt());

		return accountRepository.save(existingTask);
	}

	public String deleteAccount(String accountNo) {
		accountRepository.deleteById(accountNo);
		return accountNo + "task deleted from dashboard";
	}

}
