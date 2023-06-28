/**
 * 
 */
package com.training.accounts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.training.accounts.config.AccountsServiceConfig;
import com.training.accounts.model.Accounts;
import com.training.accounts.model.Cards;
import com.training.accounts.model.Customer;
import com.training.accounts.model.CustomerDetails;
import com.training.accounts.model.Properties;
import com.training.accounts.repository.AccountsRepository;
import com.training.accounts.service.client.CardsFeignClient;
import com.training.accounts.services.AccountService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class AccountsController {

	@Autowired
	private AccountsRepository accountsRepository;
	
	@Autowired
	private AccountService service;

	@Autowired
	AccountsServiceConfig accountsConfig;

	@Autowired
	CardsFeignClient cardsFeignClient;
	
	/*
	 * @PostMapping("/myAccount") public Accounts createAccounts(@RequestBody
	 * Accounts accInfo) { return service.addAccount(accInfo); }
	 */

	@GetMapping
	public List<Accounts> getAccountDetails() {
		return service.findAllAccounts();
	}
	
	@PutMapping
	public Accounts modifyTask(@RequestBody Accounts acc) {
		return service.updateAccount(acc);
	}
	
	@DeleteMapping("/{customerId}")
	public String deleteAccount(String custId) {
		return service.deleteAccount(custId);
	}

	
	 @PostMapping("/myAccount") public Accounts getAccountDetails(@RequestBody
	 Customer customer) {
	 
	 Accounts accounts =
	 accountsRepository.findByCustomerId(customer.getCustomerId()); if (accounts
	 != null) { return accounts; } else { return null; }
	 }
	 

	@GetMapping("/account/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties properties = new Properties(accountsConfig.getMsg(), accountsConfig.getBuildVersion(),
				accountsConfig.getMailDetails(), accountsConfig.getActiveBranches());
		String jsonStr = ow.writeValueAsString(properties);
		return jsonStr;
	}

	@PostMapping("/myCustomerDetails")
	@CircuitBreaker(name = "detailsForCustomerSupportApp", fallbackMethod = "myCustomerDetailsFallBack")
	@Retry(name = "retryForCustomerDetails", fallbackMethod = "myCustomerDetailsFallBack")
	public CustomerDetails myCustomerDetails(@RequestHeader("eazybank-correlation-id") String correlationid,
			@RequestBody Customer customer) {
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Cards> cards = cardsFeignClient.getCardDetails(correlationid, customer);

		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setCards(cards);

		return customerDetails;
	}

	private CustomerDetails myCustomerDetailsFallBack(@RequestHeader("eazybank-correlation-id") String correlationid,
			Customer customer, Throwable t) {
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		return customerDetails;

	}

	@GetMapping("/sayHello")
	@RateLimiter(name = "sayHello", fallbackMethod = "sayHelloFallback")
	public String sayHello() {
		return "Hello, Welcome to EazyBank";
	}

	private String sayHelloFallback(Throwable t) {
		return "Hi, Welcome to EazyBank";
	}

}
