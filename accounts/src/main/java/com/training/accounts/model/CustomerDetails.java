/**
 * 
 */
package com.training.accounts.model;

import java.util.List;

public class CustomerDetails {

	private Accounts accounts;
	private List<Cards> cards;

	public Accounts getAccounts() {
		return accounts;
	}

	public void setAccounts(Accounts accounts) {
		this.accounts = accounts;
	}

	public List<Cards> getCards() {
		return cards;
	}

	public void setCards(List<Cards> cards) {
		this.cards = cards;
	}

	@Override
	public String toString() {
		return "CustomerDetails [accounts=" + accounts + ", cards=" + cards + "]";
	}

}
