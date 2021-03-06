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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.github.nateowami.solve4x.Solve4x;

/**
 * Provides static utility methods for use with String equations. Many local methods in other
 * classes may be moved here if they need to have central use (especially methods in Validator.java)
 * @author Nateowami
 */
public class Util {
	
	/**
	 * Evaluates a given string to determine if it has an = sign
	 * @param The string to evaluate
	 * @return If the equation has an equals sign
	 */
	public static boolean isEq(String str){
		//debugging
		Solve4x.debug("isEq()");
		boolean hasEqualsSign = false;
		for(int i=0; i < str.length(); i++){
			if(str.charAt(i) == '='){
				hasEqualsSign = true;
			}
		}
		return hasEqualsSign;
	}
	
	/**
	 * Evaluates a char to see if it is a numeral
	 * @param c The char to evaluate
	 * @return If the char is a numeral
	 */
	public static boolean isNumeral(char c){
		switch (c){
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				return true;
			default: return false;
		}
	}

	/**
	 * Tells if a string can be parsed as an integer. No longer supports commas as a 
	 * separator in numbers. Example: "2,345" will return false
	 * @param integer The string to check 
	 * @return If the string can be parsed as an integer
	 */
	public static boolean isInteger(String integer) {
		Solve4x.debug("isInteger(" + integer + ")");
		//iterate through chars
		
		//this let's us control where the loop starts
		int i = 0;
		//if the first char is -
		if(integer.charAt(0) == '-'){
			//then make the loop start at 1
			i++;
		}
		
		//allAreNumerals may not count the first char if it's -, but thats GOOD
		boolean allAreNumerals = true;
		for(; i<integer.length(); i++){
			//if it's not a numeral
			if(!isNumeral(integer.charAt(i))){
				allAreNumerals = false;
				//no use going farther
				break;
			}
		}
		return allAreNumerals;
	}
	
	/**
	 * Checks to see if the entire expression is surrounded by parentheses,
	 * in which case they will be removed for easier parsing
	 */
	public static String removePar(String expr){
		String arg = expr;
		//debugging
		Solve4x.debug("removePar()" + expr);
		
		//check to see if it starts and ends with parentheses
		if((expr.length() >= 1) && isOpenPar(expr.charAt(0)) && isClosePar(expr.charAt(expr.length()-1))){

			int parDepth = 1; //how deep we are into parentheses nesting
			boolean parDepthReached0 = false;//if parDepth ever reaches 0 we're outside all parentheses
			//check to see if the entire expr is enclosed with parentheses
			for (int i = 1; i < expr.length() - 1; i++){
				if(isOpenPar(expr.charAt(i))){
					parDepth++;
				}
				else if(isClosePar(expr.charAt(i))){
					parDepth--;
				}
				if (parDepth == 0){
					parDepthReached0 = true;
				}
			}

			if(!parDepthReached0){
				//debugging
				Solve4x.debug("removePar(" + arg + ") -> " + expr.substring(1, expr.length()-1));
				return expr.substring(1, expr.length()-1);
			}
			else{
				//debugging
				Solve4x.debug("removePar(" + arg + ") -> " + expr);
				return expr;
			}
		}
		else{
			//debugging
			Solve4x.debug("removePar(" + arg + ") -> " + expr);
			return expr;
		}
	}
	
	/**
	 * Evaluates a char to see if it's a opening parentheses (or root or caret)
	 * @param c The char to evaluate
	 * @return If the char is a opening parentheses
	 */
	public static boolean isOpenPar(char c){
		if(c == '(' || c == '[' || c == '{' || c == '<' || c == '√'){
			return true;
		}
		else return false;
	}
	
	/**
	 * Evaluates a char to see if it's a closing parentheses (or root or caret)
	 * @param c The char to evaluate
	 * @return If the char is a closing parentheses
	 */
	public static boolean isClosePar(char c){
		if(c == ')' || c == ']' || c == '}' || c == '>' || c == '^'){
			return true;
		}
		else return false;
	}

	/**
	 * Tells if a string is a number. Examples: 1234, &lt;123&gt;/&lt;34&gt;, 34/34, 12&lt;23&gt;/&lt;67&gt;
	 * @param The number to check
	 * @return If it's a number and/or fraction combination
	 */
	public static boolean isNumber(String number) {
		//FIXME this method is broken. If number is "5" it still returns false
		Solve4x.debug("Checking if it's a number: " + number);
		
		//i is our marker for how far we've checked
		int i = 0;
		//check for the first char being -
		if(number.charAt(0) == '-'){
			//if it is, pass past it
			i++;
		}
		
		//now iterate through the first part as long as all are 0-9
		for(;i<number.length()&& isNumeral(number.charAt(i)); i++){
			//if we've reached the end
			if(i==number.length()-1){
				//we've reached the end without problems
				Solve4x.debug("Returning true");
				return true;
			}
		}
		
		//if we've gotten this far at least part of the number must be a fraction, or
		//it's not a number at all. Check the rest of it for being a fraction.
		if(isFraction(number.substring(i, number.length()))){
			Solve4x.debug("Returning true");
			return true;
		}
		
		//the end. if it didn't work, then, well, it wasn't a nice number
		else {
			Solve4x.debug("Returning false");
			return false;
		}
	}
	
	/**
	 * Evaluates a char to see if it is between a-z or A-Z
	 * @param c The char to evaluate
	 * @return If the char is a a-z or A-Z
	 */
	public static boolean isLetter(char c){
		//debugging
		Solve4x.debug("isLetter()" + c);
		if(c <= 'z' && c >= 'a' || c <= 'Z' && c >= 'A'){
			Solve4x.debug("isLetter Returns true");
			return true;
		}
		else{
			Solve4x.debug("isLetter Returns false");
			return false;
		}
	}

	/**
	 * Tells if a String has only one char and that char is a letter
	 * @param c The String to check
	 * @return If it only has one char which is a letter
	 */
	public static boolean isLetter(String c){
		if(c.length() == 1 && isLetter(c.charAt(0)))
			return true;
		else return false;
	}
	
	/**
	 * Tells if a given expression is a fraction AND that both the top and bottom of 
	 * the fraction contain ONLY INTAGERS
	 * @param frac The expression to evaluate
	 * @return If the expression is a fraction with only ints on top and bottom.
	 */
	public static boolean isFraction(String frac){
		
		//first find the fraction bar
		for(int i=0; i<frac.length(); i++){
			//if the current char is '/'
			if (frac.charAt(i) == '/'){
				//if both sides are valid fraction sides
				return isFracSide(frac.substring(0, i)) && isFracSide(frac.substring(i, frac.length()));
			}
		}
		//there's not a '/'
		return false;
		
	}
	
	/**
	 * Tells if a given expression is in the form of <integer>. Example: <56> This is useful
	 * for determining if an expression is part of a fraction.
	 * @param side The expression to evaluate
	 * @return If side is in the form of <integer>.
	 */
	public static boolean isFracSide(String side){

		//if it's an integer
		if(Util.isInteger(side)){
			return true;
		}
		
		//if it's surrounded by carets and inside is an integer
		if(side.charAt(0) == '<' && side.charAt(side.length()-1) == '>'){
			//if what's in the carets is is an integer
			if(Util.isInteger(side.substring(1, side.length()-1))){
				return true;
			}
			//it's surrounded by carets but it's not an integer
			else{
				return false;
			}
		}
		//it's not a fraction side
		else return false;
	}
	
	/**
	 * Tells if a number, fraction, or mixed number is fully simplified. 
	 * If it's a complicated fraction and it's unsure it will return false
	 * @param expr The number/fraction/mixed number to evaluate
	 * @return
	 */
	public static boolean isFullySimplified(String expr) {
		//first check that we're dealing with a number/mixed number/fraction
		if(isNumber(expr)){
			//it's a number; make sure it's simplified
			
			//the index of the first non-numeral
			int i =0;
			//update i if there's a - sign
			if(expr.charAt(0) == '-'){
				i++;
			}
			//tell if we actually found a non-numeral
			boolean found = false;
			for(; i<expr.length(); i++){
				//if it's not a numeral
				if(!isNumeral(expr.charAt(i))){
					//we found a non-numeral
					found = true;
					break;
				}
			}
			//if we didn't find a non-numeral it's just a number
			if(!found){
				return true;
			}
			//we did find a non-numeral
			else{
				//check if the fraction is simplified
				return isFracSimplified(expr.substring(i, expr.length()));
			}
		}
		//it's not even a number; return false		
		return false;
	}

	/**
	 * Tells if a fraction is fully simplified. If it contains variables it may return false
	 * even when it's fully simplified
	 * @param frac The fraction to check
	 * @return If it's fully simplified (may be hard to tell; see above)
	 */
	private static boolean isFracSimplified(String frac) {
		//TODO
		//first find the /
		int i = 0;
		for(; i < frac.length(); i++){
			if(frac.charAt(i) == '/'){
				break;
			}
		}
		//i is now the index of the /
		//if they have common factors
		if(getGCF(Integer.parseInt(frac.substring(0, i)), Integer.parseInt(frac.substring(i+1, frac.length()))) > 1){
			return false;
		}
		else return true;
	}	
	
	/**
	 * @param a The first number
	 * @param b The second number
	 * @return The GCF (greatest common factor) of the two numbers
	 */
	public static int getGCF(int a, int b){
		
		int commonFactors[] = getCommonFactors(a,b);
		//the final answer
		int answer = 1;
		//now multiply all the common factors together
		for(int i = 0; i < commonFactors.length; i++){
			answer *= commonFactors[i];
		}
		return answer;
	}
	
	/**
	 * Finds common prime factors of two numbers
	 * @param a The first number
	 * @param b The second number
	 * @return And int[] array of common factors
	 */
	public static int[] getCommonFactors(int a, int b){
		
		//XXX on debug check this (4 lines). don't know if it really works
		//perform intersection logic on two sets
		Set<Integer> s1 = new HashSet<Integer>(Arrays.asList(a));
		Set<Integer> s2 = new HashSet<Integer>(Arrays.asList(b));
		s1.retainAll(s2);
		Integer[] intersection = s1.toArray(new Integer[s1.size()]);
		
		//convert Integer[] to int[]
		int commonFactors[] = new int[intersection.length];
		for(int i = 0; i < intersection.length; i++){
			commonFactors[i] = intersection[i].intValue();
		}
		return commonFactors;		
	}
	
	/**
	 * Returns the factors of a given number
	 * @param num1 The number  for which you want the factors
	 * @return An array of factors for the number
	 */
	public static int[] factors(int num){
		//loop through from high to low
		for(int i = num-1; i > 1; i--){
			if(num % i == 0){
				//factors must be i and num/i (try it)
				//return the factors of the two factors we just found
				return concat(factors(i), factors(num/i));
			}
		}
		//if we get here there are no factors
		int ans[] = {};
		return ans;
	}
	
	/**
	 * Concatenates two arrays
	 * @param a The first array
	 * @param b The second array
	 * @return The two arrays concatenated
	 */
	private static int[] concat(int a[], int b[]){
		int out[] = new int[a.length + b.length];
		int i; //we need to keep track of i outside the loop
		//Iterate through the first one and add the elements to out[]
		for(i = 0; i < a.length; i++){
			out[i] = a [i];
		}
		//and the second array
		//we have to keep track of two vars, the place we are in out and the place in b
		for(int j = 0; j<b.length; j++){
			i++;//keeping track of i still so we know where to add things to out
			out[i] = b[j];
		}
		return out;
	}
}
