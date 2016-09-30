package com.creditcard.credit_card;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CreditCardRunner {
	
	public static void parse(String line){
		String[] words;
		
		try{
		if(line != null){
			words = line.split(" ");
				if(words[0].equalsIgnoreCase("add")){
					CreditCardProcessor.add(words[1], words[2], words[3]);
				}
				if(words[0].equalsIgnoreCase("charge")){
					CreditCardProcessor.charge(words[1], words[2]);
				}
				if(words[0].equalsIgnoreCase("credit")){
					CreditCardProcessor.credit(words[1], words[2]);
				}
			}
		} catch (IndexOutOfBoundsException e){
			System.out.println("Not enough args");
		}
	}

	public static void main( String[] args )
    {
		File inFile = null;
		BufferedReader in = null;
		String line = null;
		
		if(args.length > 0){
			inFile = new File(args[0]);
		} else {
			System.err.println("Invalid number of args");
			System.exit(1);
		}
    	
    	try {
    		if (inFile != null){
    			in = new BufferedReader(new FileReader(inFile));
    		}
    		while ((line = in.readLine()) != null){
    			CreditCardRunner.parse(line);
    		}
    		System.out.println(CreditCardProcessor.summary());
    		in.close();
    		System.exit(0);
    	} catch (FileNotFoundException e){
    		System.out.println("File not found");
    	} catch (IOException e1){
    		System.out.println("IOException reading file");
    	}
    }
	
}
