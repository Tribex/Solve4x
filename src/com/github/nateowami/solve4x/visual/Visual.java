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
package com.github.nateowami.solve4x.visual;

import com.github.nateowami.solve4x.solver.Solution;
import com.github.nateowami.solve4x.solver.Step;

/**
 * A renderer for Solve4x equations. Currently outputs to the console.
 * @author Nateowami
 */
public class Visual {
	
	/**
	 * Prints the solution to the console
	 * @param solution
	 */
	public static void render(Solution solution){
		System.out.println("************************");
		try{
			System.out.println("Solution for " + solution.getEquation().getAsString() + "\n");
			//print stuff out
			for(int i=0; i<solution.getNumbOfSteps(); i++){
				System.out.println("Step " + (i+1) + ": ");
				printStep(solution.getStepAt(i));
			}
		}
		//null pointer exceptions will be thrown if there is no solution
		catch(NullPointerException e){
			System.out.println("***NO SOLUTION FOUND***");
		}
	}
	
	/**
	 * Prints a step into the console
	 * @param step The step to print
	 */
	private static void printStep(Step step){
		for(int i=0; i<step.getStepLength(); i++){
			System.out.println("\t" + step.getStageAt(i));
			System.out.println("\tExplanation: " + step.getExplanation(i));
		}
	}
}
