package com.creditcard.credit_card;

import com.creditcard.credit_card.CreditCard;
import com.creditcard.credit_card.CreditCardProcessor;

import junit.framework.TestCase;

public class CreditCardProcessorTest extends TestCase {

	public void testValidCreditCardNumber(){
		// Valid
		assertTrue(CreditCardProcessor.validCardNumber("4111111111111111"));
		assertTrue(CreditCardProcessor.validCardNumber("5454545454545454"));
		assertTrue(CreditCardProcessor.validCardNumber("5412103570129398"));
		assertTrue(CreditCardProcessor.validCardNumber("5491272705701712"));
		
		// Invalid
		assertFalse(CreditCardProcessor.validCardNumber("1234567890123456"));
		assertFalse(CreditCardProcessor.validCardNumber("1234567890123456111111111"));
		assertFalse(CreditCardProcessor.validCardNumber("5491272705701713"));
		assertFalse(CreditCardProcessor.validCardNumber("5442103570129398"));
		assertFalse(CreditCardProcessor.validCardNumber(""));
		assertFalse(CreditCardProcessor.validCardNumber(null));
	}
	
	public void testAdd(){
		// Adding new valid cards
		CreditCardProcessor.add("Tom", "4111111111111111", "$1000");
		assertEquals(CreditCardProcessor.summary(), "Tom: $0\n");
		assertEquals(CreditCard.getCreditCards().get("Tom").getAccountHolder(), "Tom");
		assertEquals(CreditCard.getCreditCards().get("Tom").getCreditCard(), "4111111111111111");
		assertEquals(Double.valueOf(CreditCard.getCreditCards().get("Tom").getLimit()), Double.valueOf(1000));
		
		CreditCardProcessor.add("Lisa", "5454545454545454", "$3000");
		assertEquals(CreditCardProcessor.summary(), "Lisa: $0\nTom: $0\n");
		assertEquals(CreditCard.getCreditCards().get("Lisa").getAccountHolder(), "Lisa");
		assertEquals(CreditCard.getCreditCards().get("Lisa").getCreditCard(), "5454545454545454");
		assertEquals(Double.valueOf(CreditCard.getCreditCards().get("Lisa").getLimit()), Double.valueOf(3000));
		
		// Adding new card same name
		CreditCardProcessor.add("Tom", "4111111111111112", "$500");
		assertEquals(CreditCardProcessor.summary(), "Lisa: $0\nTom: $0\n");
		assertEquals(CreditCard.getCreditCards().get("Tom").getAccountHolder(), "Tom");
		assertEquals(CreditCard.getCreditCards().get("Tom").getCreditCard(), "4111111111111111");
		assertEquals(Double.valueOf(CreditCard.getCreditCards().get("Tom").getLimit()), Double.valueOf(1000));
		
		// Adding invalid credit card number
		CreditCardProcessor.add("Quincy", "1234567890123456", "$2000");
		assertEquals(CreditCardProcessor.summary(), "Lisa: $0\nQuincy: error\nTom: $0\n");
		assertNull(CreditCard.getCreditCards().get("Quincy"));
		
		// Adding empty or null args
		CreditCardProcessor.add("Jedd", "5412103570129392", null);
		CreditCardProcessor.add("Jedd", "", null);
		CreditCardProcessor.add("", "5412103570129392", "$1000");
		CreditCardProcessor.add("Jedd", "5412103570129392", "");
		CreditCardProcessor.add(null, "5412103570129392", "");
		CreditCardProcessor.add("Jedd", null, "$1");
		assertEquals(CreditCardProcessor.summary(),"Lisa: $0\nQuincy: error\nTom: $0\n");
	}
	
	public void testCharge(){
		// Charges that raise balance over limit
		CreditCardProcessor.charge("Tom", "$2000");
		CreditCard card = CreditCard.getCreditCards().get("Tom");
		assertEquals(Double.valueOf(card.getBalance()), Double.valueOf(0));
		
		// Charges that raise balance within limit
		CreditCardProcessor.charge("Tom", "$999");
		card = CreditCard.getCreditCards().get("Tom");
		assertEquals(Double.valueOf(card.getBalance()), Double.valueOf(999));
		
		// Raise balance over limit
		CreditCardProcessor.charge("Tom", "$1000");
		card = CreditCard.getCreditCards().get("Tom");
		assertEquals(Double.valueOf(card.getBalance()), Double.valueOf(999));
		
		// Charge negative amounts
		CreditCardProcessor.charge("Tom", "$-1000");
		card = CreditCard.getCreditCards().get("Tom");
		assertEquals(Double.valueOf(card.getBalance()), Double.valueOf(-1));
		
		// Charges against invalid card numbers
		CreditCardProcessor.charge("Quincy", "$999");
		card = CreditCard.getCreditCards().get("Quincy");
		assertNull(card);
		
		// Charges against accounts that don't exist
		CreditCardProcessor.charge("Danube", "$999");
		card = CreditCard.getCreditCards().get("Danube");
		assertNull(card);
		assertEquals(CreditCardProcessor.summary(), "Lisa: $0\nQuincy: error\nTom: $-1\n");
	}
	
	public void testCredit(){
		// Credits that decrease balance of card below $0 creates negative balance
		CreditCardProcessor.credit("Tom", "$2000");
		CreditCard card = CreditCard.getCreditCards().get("Tom");
		assertEquals(Double.valueOf(card.getBalance()), Double.valueOf(-2001));
		
		// Credits against invalid cards are ignored
		CreditCardProcessor.credit("Quincy", "$2000");
		card = CreditCard.getCreditCards().get("Quincy");
		assertNull(card);
		
		// Credits against accounts that don't exist
		CreditCardProcessor.credit("Danube", "$2000");
		card = CreditCard.getCreditCards().get("Danube");
		assertNull(card);
		
		// Credit negative amounts
		CreditCardProcessor.credit("Tom", "$-2000");
		card = CreditCard.getCreditCards().get("Tom");
		assertEquals(Double.valueOf(card.getBalance()), Double.valueOf(-1));
	}
	
	public void testSummary(){
		assertEquals(CreditCardProcessor.summary(),"Lisa: $0\nQuincy: error\nTom: $-1\n");
		
		//after inserting name before
		CreditCardProcessor.add("Danube", "5412103570129398", "$1000");
		assertEquals(CreditCardProcessor.summary(),"Danube: $0\nLisa: $0\nQuincy: error\nTom: $-1\n");
		
		//after inserting name in middle
		CreditCardProcessor.add("Monica", "5412103570129398", "$1000");
		assertEquals(CreditCardProcessor.summary(),"Danube: $0\nLisa: $0\nMonica: $0\nQuincy: error\nTom: $-1\n");

		//after inserting name at end
		CreditCardProcessor.add("Zed", "5412103570129392", "$1000");
		assertEquals(CreditCardProcessor.summary(),"Danube: $0\nLisa: $0\nMonica: $0\nQuincy: error\nTom: $-1\nZed: error\n");
	}
}
