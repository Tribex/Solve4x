/*
    Solve4x - An audio-visual algebra solver
    Copyright (C) 2014 Nathaniel Paulus

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.nateowami.solve4x.solver;

import java.math.BigDecimal;

/**
 * Represents a number/fraction/mixed-number such as 35, 45/6, 56&lt;36&gt;&lt;32&gt;, and 
 * provides methods for adding, subtracting, multiplying, and dividing numbers. TODO
 * @author Nateowami
 */
public class Number {
	
	//The numerical part of a mixed number (could be a decimal)
	String wholeNumber;
	//the top of a fraction
	String top;
	//and the bottom
	String bottom;
	
	public Number(String num){
		//parse the number
		int i;
		for(i = 0; i<num.length(); i++){
			//if the current char is not a numeral
			if(!Util.isNumeral(num.charAt(i))){
				break;
			}
		}
		//now we have the index of the first non-numeral, or else just the end of the string
		//(if they're all numerals)
		wholeNumber = num.substring(0, i);
		//parse the fraction part
		parseFraction(num.substring(i+1, num.length()));
	}
	
	/**
	 * Parses the fraction part of a number and initialises the vars top and bottom
	 * @param frac The fraction to parse
	 */
	private void parseFraction(String frac){
		//the index of the /
		int indexOfDiv = 0;
		for(int i = 0; i< frac.length(); i++){
			if(frac.charAt(i) == '/'){
				indexOfDiv = i;
				break;
			}
		}
		//now parse the top and bottom of the fraction, namely everything
		//before indexOfDiv and everything after it
		for(int i = 0; i<2; i++){
			//set sideOfFrac
			String sideOfFrac;
			if(i == 0) sideOfFrac = frac.substring(0, indexOfDiv);
			else sideOfFrac = frac.substring(indexOfDiv+1, frac.length());
			//remove any carets that may be on both sides
			if(sideOfFrac.charAt(0) == '<' && sideOfFrac.charAt(sideOfFrac.length()) == '>'){
				sideOfFrac = sideOfFrac.substring(1, sideOfFrac.length()-1);
			}
			if(i == 0) top = sideOfFrac;
			else bottom = sideOfFrac;
		}
	}

	/**
	 * @return the wholeNumber part of the mixed number
	 */
	public String getWholeNumber() {
		return wholeNumber;
	}

	/**
	 * @return the numerator of the fraction
	 */
	public String getTop() {
		return top;
	}

	/**
	 * @return the denominator of the fraction
	 */
	public String getBottom() {
		return bottom;
	}
	
	 /**
	 * Adds two numbers and returns the result
	 * @param n1 The first Number
	 * @param n2 The second Number
	 * @return A Number equal to the value of the two numbers added
	 * @throws IllegalArgumentException If the denominators of the fractions of the two numbers 
	 * (if they have fractions, that is) are not equal
	 */
	public Number add(Number n1, Number n2) throws IllegalArgumentException{
		
		
		return new Number("");
	}
	
	/**
	 * Adds two numbers represented by a String. This should not be confused with adding two Numbers;
	 * this only adds integer/decimal values represented by a string
	 * @param n1 The first number to add
	 * @param n2 The second number to add
	 * @return The value of two numbers added
	 * @throws IllegalArgumentException If the Strings cannot be parsed as ints or decimals
	 */
	private String add(String n1, String n2) throws IllegalArgumentException{
		//first see if they are both ints
		if(Util.isInteger(n1) && Util.isInteger(n2)){
			//since they can be parsed as ints just convert to ints, add, and convert to String
			return (Integer.parseInt(n1) + Integer.parseInt(n2)) + "";
		}
		//they're not ints then.
		//First make the decimal places line up.
		//we do this by finding the first instance of a decimal in each
		//whichever one has the decimal earlier needs zeros put at the beginning of it to align the decimal places
		//set where the decimal place belongs
		int index1 = n1.indexOf('.') == -1 ? n1.length() : n1.indexOf('.');
		int index2 = n2.indexOf('.') == -1 ? n2.length() : n2.indexOf('.');
		
		//now insert the decimals if they're not already there
		if(index1 == n1.length()){
			//There is no decimal at the end. Add it.
			n1 = n1 + ".";
		}
		if(index2 == n2.length()){
			//There is no decimal at the end. Add it.
			n2 = n2 + ".";
		}
		//Now they both have decimals. We can use index1 and index2 to figure out where they are.
		if(index1 > index2){
			//the numbers look something like this: 23.56
			//                                      2.04
			//so we need to add (index1 - index2) # of zeros to the beginning of n2
			for(int i = 0; i < index1 - index2; i++){
				//add index1 - inex2 # of zeros to the beginning of n2
				n2 = "0" + n2;
			}
		}
		//now do the reverse if index1 < index2
		else if(index1 < index2){
			//the numbers look something like this: 2.56
			//                                      23.04
			//so we need to add (index2 - index1) # of zeros to the beginning of n1
			for(int i = 0; i < index2 - index1; i++){
				//add index1 - inex2 # of zeros to the beginning of n2
				n1 = "0" + n1;
			}
		}
		//now we know the numbers look something like this: 23.576
		//                                                  04.57
		//we still need to add zeros to the end of whichever one is shorter
		//TODO
		return null;
	}
	
}