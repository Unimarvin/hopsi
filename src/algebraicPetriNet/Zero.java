package algebraicPetriNet;

import java.util.*;

/**
 * Methods for the zeros of an inequation.
 *
 *
 */
public class Zero {
	/**
	 * 
	 * @param equation
	 * @param numberOfPlaces
	 * @return returns all zeros of the given equation
	 */
	public static ArrayList<ArrayList<Integer>> getAll(Equation equation, int numberOfPlaces){
		int upperBound = 1;
		ArrayList<ArrayList<Integer>> algebraicZeros = new ArrayList<>();

		//calculating the upper bound for the zeros
		//w.r.t. homogeneity of the inequation: 2*#places*max-koeff
	    for (int i=0; i< equation.amount-1; i++) {
	    	if(Math.abs(equation.amountLefternSide[i]) > upperBound)	
	    	upperBound = Math.abs(equation.amountLefternSide[i]);
	    }
        if(equation.amount > 0 && equation.amount < numberOfPlaces) {
          numberOfPlaces = equation.amount;
        }
	    upperBound=upperBound*upperBound*2*numberOfPlaces;

	    System.out.println("");
	    System.out.println("Showing all possible solutions of the given (In-)equation:");
	    System.out.println("(Zeros, if it is an equation)");
	    System.out.println("The upper bound 2*n*k^2 is: "+upperBound);

	    //For every weight (j=1,2,...,max) all the candidates are calculated
	    int size = 1;
	    for (int j = 1; j < upperBound; j++) {
	    	try{
	    		size += permutationsAmount(numberOfPlaces, j);
	    		}
	    	catch(IllegalArgumentException e){
	    		size = 5000;
	    	}

		    ArrayList<ArrayList<Integer>> zeros = division(j,numberOfPlaces);
		    ArrayList<Integer> zero = new ArrayList<>(numberOfPlaces);
		    int value =0;
		    for (int i = 0; i <zeros.size(); i++) {
				zero.clear();
		    	zero.addAll(zeros.get(i));
		    	for(int k=0; k<zero.size(); k++){
		    		value+=zero.get(k)*equation.amountLefternSide[k];
		    	}
		    	//check if its is an inequation 
		    	//(by now we only consider righternsides equal to zero)
		    	if(equation.inequation == true){
		    	    if(value<0){
	                    zeros.remove(i);
	                    i--;
	                }
		    	}
		    	//if it is not a zero, we throw it out of the list and continue with the next one.
		    	else if(value!=0){
		    		zeros.remove(i);
		    		i--;
		    	}
		    	value=0;
			}
		    algebraicZeros.addAll(zeros);
	    }
		System.out.println("There cannot be more than "+size+" possible solutions.");
		System.out.println("");
	    return algebraicZeros;
	}

	/**
	 * 
	 * @param token
	 * @param places
	 * @return all possible distributions of k (int token) non-distinguishable token
	 * onto n (int places) places within respect to the order.
	 */
	public static ArrayList<ArrayList<Integer>> division(int token, int places){
		if(token<0 || places<0){
			throw new IllegalArgumentException("The variables n and k most both be non-negative");
		}

		//base case 1
		if (token == 0){
			ArrayList<ArrayList<Integer>> bigEnd = new ArrayList<>();
			ArrayList<Integer> smallEnd = new ArrayList<>(places);
			for (int i = 0; i < places; i++) {
				smallEnd.add(0);
			}
			bigEnd.add(smallEnd);
			return bigEnd;
		}

		//base case 2
		if (places == 1) {
			ArrayList<ArrayList<Integer>> bigEnd = new ArrayList<>();
			ArrayList<Integer> smallEnd = new ArrayList<>();
			smallEnd.add(token);
			bigEnd.add(smallEnd);
			return bigEnd;
		}

		//recursion
		//greedy algorithm
		ArrayList<ArrayList<Integer>> big = new ArrayList<>();
		ArrayList<ArrayList<Integer>> temp = new ArrayList<>();
		ArrayList<Integer> small = new ArrayList<>();
		for (int i = token; i >= 0; i--) {
			temp=division(token-i,places-1);
			for (int k = 0; k < temp.size(); k++) {
				small=temp.get(k);
				small.add(i);
				big.add(small);
			}
		}
		return big;
	}

	/**
	 * 
	 * @param n number of places
	 * @param k number of balls
	 * @return returns the number of possibilities of distributing k non-distinguishable Balls onto n places, within respect of the order
	 * of the places 
	 * 
	 */
	public static int permutationsAmount(int n, int k){
		if(n<0 || k<0){
			throw new IllegalArgumentException("The variables n and k must both be non-negative");
		}

		long result = 1;
		long m=n;
		long l=k;

		//Numerator of the fraction
		for (long i = l+m-1; i > m-1; i--) {
			result = result*i;
			if(result<0){
				throw new IllegalArgumentException("The value of k+n-1 over n-1 is too big, try different value of n and/or k");
			}
		}
		//Denominator of the fraction
		for(long i = l; i > 1; i--){
			result = result/i;
		}
		return (int) result;
	}
}
