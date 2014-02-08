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
package com.github.nateowami.solve4x.algorithm;

import java.util.ArrayList;

import com.github.nateowami.solve4x.solver.Algorithm;
import com.github.nateowami.solve4x.solver.Equation;
import com.github.nateowami.solve4x.solver.Expression;
import com.github.nateowami.solve4x.solver.Step;
import com.github.nateowami.solve4x.solver.Term;
import com.github.nateowami.solve4x.solver.Number;

/**
 * @author Nateowami
 */
public class CombineLikeTerms extends Algorithm{

	/**
	 * Combines like terms in a given equation
	 * @param equation The equation to combine terms in
	 * @return A Step with this algorithm applied
	 */
	@Override
	public Step getStep(Equation equation) {
		//figure out how many like terms there are in each expression
		
		//which expression has the most like terms
		int whichHasMost = 0;
		//and how many like terms it has
		int numOfLikeTerms = 0;
		//now loop through to figure out which expression needs to be simplified the most
		for(int i = 0; i< equation.getSize(); i++){
			//temp variable
			int temp;
			//if the current expression needs simplifying more than any up to this point
			if((temp = howManyLike(equation.getExpression(i))) > numOfLikeTerms){
				//set the vars most and numbOfLikeTerms
				whichHasMost = i;
				numOfLikeTerms = temp;
			}
		}
		
		//now that we've done that we know that we need to simplify the expression at index whichHasMost
		Expression expr = equation.getExpression(whichHasMost);
		
		//now figure out which type of term needs to be combined most
		
		//how many there are of a particular type
		int numOfType = 0;
		//and what that type is
		String type = "";
		//loop through the terms
		for(int i = 0; i< expr.numbOfTerms(); i++){
			//temp variable
			int temp;
			//if the number of of the current type is greater than any found earlier
			if((temp = numLikeThis(expr, expr.termAt(i))) > numOfType){
				//update vars numOfType and type
				numOfType = temp;
				type = expr.termAt(i).getBody();
			}
		}
		
		//now we know that we need to combine all terms of type body
		//arraylist of terms for the output
		ArrayList <Term>terms = new ArrayList<Term>();
		//and the index for us to combine terms at
		int index = 0;
		//ArrayList of terms we will combine
		ArrayList <Term>combinedTerms = new ArrayList<Term>();
		for(int i = 0; i<expr.numbOfTerms(); i++){
			//if the current term is not the type that we're combining
			if(!expr.termAt(i).getBody().equals(type)){
				//add it to the list of terms that we don't modify
				terms.add(expr.termAt(i));
			}
			//this is one of the terms we need to combine
			else{
				//add it to the list of the terms to combine
				combinedTerms.add(expr.termAt(i));
				//and if this is the first term we've added
				if(index == 0){
					//set the index of the first term that we're combining
					//we do this so all of these terms combined into one will be at the index the first one was found at
					index = i;
				}
			}
		}
		
		//now we need to combine the all the terms in combinedTerms and add the result to 
		//terms at the index specified by index
		
		//first make a list of the numbers/coefficients of the terms to combine
		String coefficients[] = new String[combinedTerms.size()];
		//add the coefficients of each term to the coefficients array
		for(int i = 0; i < coefficients.length; i++){
			coefficients[i] = combinedTerms.get(i).getCoe();
		}
		//the value of all the coefficients we'll add
		Number numSoFar = new Number("0");
		//add all the coefficients
		for(int i = 0; i < coefficients.length; i++){
			//if coefficients[i] is "", then it should be 1, because no coefficient is the same as a coefficient of 1
			if(coefficients[i].equals("")){
				//set it to 1
				coefficients[i] = "1";
			}
			//add numSoFar and the current number
			numSoFar = Number.add(numSoFar, new Number(coefficients[i]));
		}
		//now the coefficient of the combined term will be numSoFar
		//add the coefficient and the term type to create a term
		Term finalTerm = new Term(numSoFar.getAsString()+type);
		//now add that to the list of terms at index "index"
		terms.add(index, finalTerm);
		
		//convert that to an expression
		String finalExpression = "";
		//put the expression together
		for(int i = 0; i < terms.size(); i++){
			//XXX probably doesn't take signs into account
			finalExpression += terms.get(i);
		}
		//the explanation for this algorithm
		String lameExplanation = "We're combining the terms and the result is " + finalTerm + ".";
		//an array containing only one expression
		String steps[] = {finalExpression};
		//the final step
		return new Step(steps, lameExplanation, 4);
	}

	/**
	 * Tells how smart it is to use this algorithm to the specified equation
	 * @param equation The equation to check
	 * @return On a scale 0-9 how smart it is to use this algorithm on this equation
	 */
	@Override
	public int getSmarts(Equation equation) {
		//find which expression needs simplifying the most
		int num = 0;
		for(int i=0; i<equation.getSize(); i++){
			//temp variable
			int temp;
			if((temp = howManyLike(equation.getExpression(i))) > num){
				num = temp;
			}
		}
		//figure out the smartness based on number of like terms
		if(num == 2){
			return 4;
		}
		else if(num == 3){
			return 6;
		}
		else if(num == 4){
			return 8;
		}
		else if(num > 4){
			return 9;
		}
		else{
			return 0;
		}
	}
	
	/**
	 * Tells how many terms in a given expression are alike
	 * For example 3x+4x+5x+5x4+2x4 will return 3
	 * @param expr The expression to check
	 * @return The number of like terms
	 */
	private int howManyLike(Expression expr){
		int num = 0;
		for(int i = 0; i< expr.numbOfTerms(); i++){
			//update num
			//temp variable
			int temp;
			if(num < (temp = numLikeThis(expr, expr.termAt(i)))){
				num = temp;
			}
		}
		return num;
	}
	
	/**
	 * Tells how many terms in a given expression are the same type as the specified term
	 * @param expr The expression
	 * @param term The term
	 * @return The number of terms in expr that are the same type as term
	 */
	private int numLikeThis(Expression expr, Term term){
		int num = 0;
		//check all terms in the expression for being like term
		for(int i =0;  i< expr.numbOfTerms(); i++){
			if(expr.termAt(i).getBody().equals(term.getBody())){
				i++;
			}
		}
		return num;
	}
}
