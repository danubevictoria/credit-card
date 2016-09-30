package com.creditcard.credit_card;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

public class CreditCard
{
	private static SortedMap<String, CreditCard> creditCards = new TreeMap<String, CreditCard>();
	private final String accountHolder;
	private final String creditCardNumber;
	private final Integer limit;
	private Integer balance;
	
	/** New cards start with a $0 balance **/
	public CreditCard(String name, String creditCardNumber, Integer amount){
		accountHolder = name;
		this.creditCardNumber = creditCardNumber;
		limit = amount;
		balance = 0;
	}
	
	public static SortedMap<String, CreditCard> getCreditCards(){
		return Collections.unmodifiableSortedMap(creditCards);
	}
	
	public String getAccountHolder(){
		return this.accountHolder;
	}
	
	public String getCreditCard(){
		return this.creditCardNumber;
	}
	
	public Integer getLimit(){
		return this.limit;
	}
	
	public Integer getBalance(){
		return this.balance;
	}
	
	protected void setBalance(Integer amount){
		balance = amount;
	}
	
	public static void addCreditCard(String name, CreditCard card){
		creditCards.put(name, card);
	}
}
