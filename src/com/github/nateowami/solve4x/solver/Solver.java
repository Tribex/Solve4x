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

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;

/**
 * Solves equations and simplifies expressions
 * @author Nateowami
 */

public class Solver {
	
	//the final solution
	private Solution finalSolution;
	//a list of solutions that may work. Whichever one is best will be used in the end
	ArrayList <Solution> solutions = new ArrayList<Solution>();
	//A list of strategies that can be used for solving
	ArrayList <Strategy> strat;
	
	
	/**
	 * Creates a new Solver so you can call solve and get the solution.
	 * @param equation The equation or expression to solve, simplify, 
	 * factor, multiply, etc
	 * @param selection The users selection. Do they want this to be 
	 * factored, solved, simplified, or what?
	 * @see com.github.nateowami.solve4x.solver.SolveFor
	 */
	public Solver(String equation, SolveFor selection) throws MalformedInputException{
		
		
		//set the solving strategies list based on what the user selected
		//if they wanted it solved
		if(selection == SolveFor.SOLVE){
			strat = getSolvingList();
		}
		//if the user wanted it simplified
		else if(selection == SolveFor.SIMPLIFY){
			strat = getSimplifyingList();
		}
		
		//OK, now to solve. Nateowami is scared
		//add an initial solution to the solution list
		solutions.add(new Solution(new Equation(equation)));
		
		//OK, now iterate through the solving strategies AND the solutions 
		//(currently only 1 solution, but this could grow as we take forks in the road)
		//here goes...
		
		//loop through the solutions
		for(int a=0; a<solutions.size(); a++){
			//first copy the current solution
			Solution currentSol = solutions.get(a);
			//then delete it from the list. It will once again exist after being
			//modified and/or multiplied, but it won't again exist in its current state
			//XXX which means we have to be careful to main two lists, then copy them over to original
			solutions.remove(a);
			Equation currentEq = currentSol.getLastEquation();
			
			//now loop through the strategies
			for(int b=0; b<strat.size(); b++){
				
			}
			
		}
		
	}
	
	/**
	 * Solves the given equation or simplifies it if it is an expression
	 * @param expr The expression or equation to solve or simplify
	 * @return A Solution object that contains the steps for solving
	 * @throws MalformedInputException 
	 */
	public Solution getSolution(){
		
		return finalSolution;
	}
	
	/**
	 * Makes a list of solving strategies
	 * @return An ArrayList of strategies for solving
	 * @see getSimplifyingList()
	 */
	private static <Strategy>ArrayList getSolvingList(){
		
		//the list of strategies
		ArrayList <Strategy>stratList = new ArrayList<Strategy>();
		
		//add the list of simplifying strategies (all simplifying strategies are also for solving)
		stratList.addAll(getSimplifyingList());
		
		//TODO add more stuff to stratList
		
		
		return stratList;
		
	}
	
	/**
	 * Makes a list of strategies for simplifying
	 * @return An ArrayList of strategies for simplifying
	 * @see getSolvingList()
	 */
	private static <Strategy>ArrayList getSimplifyingList(){
		
		//make the list of strategies
		ArrayList <Strategy>stratList = new ArrayList<Strategy>();
		
		//TODO add stuff to the stratList
		
		return stratList;
	}
	
	/**
	 * Tells if an equation is solved 
	 * TODO tell is an expression is fully simplified
	 * @return If the equation is solved
	 */
	private boolean isSolved(String equation){
		//XXX We're assuming this is an equation, which currently is
		//true, but we need to support more late. Possibly in another method
		
		//create a new equation
		Equation eq = new Equation(equation);
		
		//check for an identity (i.e. 1=1, 3/4=3/4)
		if(eq.getExpression(0) == eq.getExpression(1)){
			return true; //if it's an identity it's SOLVED (technically)
		}
		
		//if the first is a variable and the second is a number
		else if(Util.isLetter(eq.getExpression(0).getExpression()) && Util.isNumber(eq.getExpression(1).getExpression())){
			return true;
		}
		
		//if the second is a variable and the first is a number
		else if(Util.isLetter(eq.getExpression(1).getExpression()) && Util.isNumber(eq.getExpression(0).getExpression())){
			return true;
		}
		
		//At this time the above three are all that I can think of. If there are more
		//we will need to talk about them and add them.
		else return false;
		
	}
	
	/**
	 * TODO NOT FINISHED!!!!!!!*********
	 * Tells if we're done solving. It looks through the list of solutions and finds one that is 
	 * solved, simplified, or whatever needs to be done to it. Returns true if it finds one, false
	 * any other time.
	 * @param solList The list of Solutions to check
	 * @return If one of the solutions is solved/simplified
	 */
	private boolean isFinished(ArrayList<Solution> solList){
		
		
		return false;//TODO
		
	}
	
	/**
	 * Tells if an expression is fully simplified. Examples: 23&lt;2&gt;/&lt;3&gt;
	 * @param expr
	 * @return
	 */
	private boolean isFullySimplified(String expr){
		//if it's a number
		if(Util.isNumber(expr)){
			//AND it's fully simplified (TODO)
			if(Util.isFullySimplified(expr)){
				return true;
			}
			//not fully simplified
			else{
				return false;
			}
		}
		//if it's a variable
		else if(expr.length() == 0 && Util.isLetter(expr.charAt(0))){
			return true;
		}
		//I can't think of any other way for it to be fully simplified
		else return false;
	}
	
}
