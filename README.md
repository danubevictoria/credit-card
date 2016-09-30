# Basic Credit Card Processing
-----

This program will add new credit card accounts, process charges and credits
against them, and display summary information.

# Solution
This is a Maven project built through Eclipse IDE and also includes automated J-Unit test cases. I chose to use Java as that is a language I am more comfortable using.

To make it easier to maintain, manage, test, and reuse, I broke up my solution into multiple classes:
- **CreditCard** - a class to store basic information about an account, including name, credit card number, limit, and balance, and a list of all accounts added
- **CreditCardProcessor** - a class for credit card processing, including whether or not a credit card was valid, add, charge, or credit a credit card, and provide a summary of the credit cards
- **CreditCardRunner** - a class for running the credit card processing that reads in a text file

To create automated test cases, I split the J-Unit tests into two classes:
- **CreditCardProcessorTest** - a class for testing methods and constructors from the CreditCard and the CreditCardProcessor classes
- **TestRunner** - a class to run the test cases from command line. Will return true if results are successful. Will return false and print error(s) if results are unsuccessful.

## CreditCard
Within the CreditCard class, I chose to use private variables to encapsulate data as we don't typically want to expose credit card information externally. However, we still might want to know what information is being stored for each credit card account, so I implemented public getter methods that any class can call externally. Under the assumption that we don't need to change the accountHolder's name, creditCardNumber, or limit, I chose to make these variables final. Because we wanted to summarize account information in alphabetical order, I chose to use a SortedMap or TreeMap to store the credit card information, which sorts on the **name** as key. I made it static so that you wouldn't need to create a Credit Card object in order to access the list of credit cards. We don't want the TreeMap to be changed externally outside of this class, so the getter for the credit cards is unmodifiable. Since it's unmodifiable, I had to create a public addCreditCard method so that credit cards can be added externally.  For simplicity, I chose to use Integer for the **limit** and **balance**, but we could probably use something with better precision especially in dealing with currency transactions, such as BigDecimal. Under the assumption that we will only ever need to change the balance of a credit card when we charge or credit, I created only one protected setter to limit access for changing the balance to subclasses and package members. 

## CreditCardProcessor
To validate credit card numbers, I used the Luhn 10 formula.
1. Reverse the order of the digits in the number.
2. Take the first, third, ... and every other odd digit in the reversed digits and sum them to form the partial sum s1
3. Taking the second, fourth ... and every other even digit in the reversed digits:
	i. Multiply each digit by two and sum the digits if the answer is greater than nine to form partial sums for the even digits
	ii. Sum the partial sums of the even digits to form s2
4. If s1 + s2 ends in zero then the original number is in the form of a valid credit card number as verified by the Luhn test.

For example, if the trial number is 49927398716:
```
Reverse the digits:
  61789372994
Sum the odd digits:
  6 + 7 + 9 + 7 + 9 + 4 = 42 = s1
The even digits:
    1,  8,  3,  2,  9
  Two times each even digit:
    2, 16,  6,  4, 18
  Sum the digits of each multiplication:
    2,  7,  6,  4,  9
  Sum the last:
    2 + 7 + 6 + 4 + 9 = 28 = s2

s1 + s2 = 70 which ends in zero which means that 49927398716 passes the Luhn test
```

For adding a new credit card, we check first that the credit card number is valid and if it is, and hasn't been added yet, we add it in. Otherwise, we still store the name, but don't have a credit card to associate the name to. This is to keep a history for the summary later.

For charging a credit card, if the person has a valid credit card, we update the balance as long as it doesn't go above the limit. Otherwise, the charge is ignored. 

For crediting a credit card, if the person has a valid credit card, we update the balance and allow the credit card to have a negative balance. Otherwise, we ignore the credit. 

I chose to make these 3 methods synchronized because we wouldn't want to run into any race conditions or concurrency issues that could result in corrupted balances or credit cards added.

For summarizing the information, I used a StringBuilder instead of a String, since we are updating the summary string as we iterate through the SortedMap and StringBuilders are mutable while Strings are immutable. 

## CreditCardRunner
Within CreditCardRunner class, I parse the lines that are being read in from a file to process. 

## CreditCardProcessorTest
For easier readability and to make it easier to localize errors, I separated out test methods for each method in CreditCardTestProcessor. Within each of these test cases, I test methods from CreditCard class as well. 

Test cases for validCreditCardNumber:
- Valid credit card numbers
- Invalid credit card numbers

Test cases for testing add:
- Valid credit cards
- Invalid credit cards
- Credit Cards for people with the same name that's already been added
- Invalid arguments

Test cases for charge:
- Charges that raise balance over limit
- Charges that raise balance within limit
- Charge negative amounts
- Charge against invalid card numbers
- Charge against credit cards that don't exist

Test cases for credit: 
- Credits that decrease balance of card below $0 creates negative balance
- Credits against invalid cards are ignored
- Credits against accounts that don't exist
- Credit negative amounts

Test cases for summary:
- Inserting name in front of alphabetical order
- Inserting name in middle
- Inserting name at end

## TestRunner
Runs the test cases from command line. Will return true if results are successful. Will return false and print error(s) if results are unsuccessful. 

# Usage
To run this program, you'll need to have the same versions of <a href="http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html">JRE</a> and <a href="http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html">JDK</a> installed and set up correctly. Extract all of the zipped files to a directory. Open cmd prompt.

Navigate to the java files to compile. For example: 
```
thinkorview@SonyVaio8 MINGW64 ~/workspace/credit-card/src/main (master)
$ cd java
```

Compile the files. Note: You must fully specify package name
```
thinkorview@SonyVaio8 MINGW64 ~/workspace/credit-card/src/main/java (master)
$ javac com/creditcard/credit_card/*.java
```

Once compiled successfully, run the program with the example text file. Note: Java is case-sensitive and ou must fully specify package name
```
thinkorview@SonyVaio8 MINGW64 ~/workspace/credit-card/src/main/java (master)
$ java com.creditcard.credit_card.CreditCardRunner Example.txt
``` 

Output for example file should be:
```
Lisa: $-93
Quincy: error
Tom: $500
```

Open the project in Eclipse IDE and run the TestRunner.java.