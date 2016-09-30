package com.creditcard.credit_card;

import java.util.SortedMap;

public class CreditCardProcessor {
	
	/** Card numbers should be validated using Luhn 10 **/
	public static Boolean validCardNumber(String cardNumber){
		if(cardNumber == null){
			return false;
		} else if(cardNumber.length() > 19 || cardNumber.length() == 0){
			return false;
		} else {
			String reversed = new StringBuilder(cardNumber).reverse().toString();
			boolean even = false;
			Integer sum1 = 0;
			Integer sum2 = 0;
			for (int i = 0; i < reversed.length(); i++){
				int number = reversed.charAt(i) - 48;
				if (even) {
					number = (number >= 5) ? ((number * 2) - 9) : number * 2;
				}
				sum1 += number;
				even = !even;
			}
			if (Math.floorMod((sum1 + sum2), 10) == 0){
				return true;
				
			} else {
				return false;
			}
		}
	}
	
	/** "Add" will create a new credit card for a given name, card number, and limit **/
	public static synchronized void add(String name, String creditCard, String limit){
		CreditCard newCard = null;
		SortedMap<String, CreditCard> creditCards = CreditCard.getCreditCards();
		
		if(name != null && creditCard != null && limit != null 
		&& name.length() > 0 && creditCard.length() > 0 && limit.length() > 0){
			if(!creditCards.containsKey(name)){
				if(CreditCardProcessor.validCardNumber(creditCard)){
					newCard = new CreditCard(name, creditCard, Integer.parseInt(limit.replace("$", "")));
					CreditCard.addCreditCard(name, newCard);
				} else {
					CreditCard.addCreditCard(name, null);
				}
			}
		}
	}
	
	/** "Charge" will increase the balance of the card associated with the provided 
	 * name by the amount specified. Charges that would raise the balance over the 
	 * limit are ignored as if they were declined. Charges against Luhn 10 invalid 
	 * cards are ignored. **/
	public static synchronized void charge(String name, String amount){
		CreditCard card = null;
		Integer amountInt = Integer.parseInt(amount.replace("$", ""));
		SortedMap<String, CreditCard> creditCards = CreditCard.getCreditCards();
		
		if(creditCards.containsKey(name)){
			card = creditCards.get(name);
			if(card != null){
				if(!((card.getBalance() + amountInt) > card.getLimit())){
					card.setBalance(card.getBalance() + amountInt);
				}
			}
		}
	}
	
	/** "Credit" will decrease the balance of the card associated with the provided 
	 * name by the amount specified. 
	 * Credits that would drop the balance below $0 will create a negative balance
	 * Credits against Luhn 10 invalid cards are ignored
	 **/
	public static synchronized void credit(String name, String amount){
		CreditCard card = null;
		Integer amountInt = Integer.parseInt(amount.replace("$", ""));
		SortedMap<String, CreditCard> creditCards = CreditCard.getCreditCards();
		
		if(creditCards.containsKey(name)){
			card = creditCards.get(name);
			if(card != null){
				card.setBalance(card.getBalance() - amountInt);
			}
		}
	}
	
	/** - When all input has been read and processed, a summary should be generated and
		  written to STDOUT.
		
		- The summary should include the name of each person followed by a colon and
		  balance
		
		- The names should be displayed alphabetically
		
		- Display "error" instead of the balance if the credit card number does not pass
		  Luhn 10
	**/
	public static String summary(){
		SortedMap<String, CreditCard> creditCards = CreditCard.getCreditCards();
		StringBuilder summary = new StringBuilder("");
		
		if(creditCards != null){
			for(String name : creditCards.keySet()){
				if(creditCards.get(name) != null){
					summary.append(name + ": $" + creditCards.get(name).getBalance() + "\n");
				} else {
					summary.append(name + ": error\n");
				}
			}
		}
		return summary.toString();
	}
}
