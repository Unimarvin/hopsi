package algebraicPetriNet;

import java.util.ArrayList;

public class Logic {
	/**
	 * 
	 * @param equ
	 * @param rawZeros
	 * @return most general unifier
	 */
	public ArrayList<ArrayList<Substitution>> unification(Equation equ, ArrayList<ArrayList<Integer>> rawZeros){
        ArrayList<Term> terms = new ArrayList<>();
	    ArrayList<ArrayList<Substitution>> unifier = new ArrayList<>();
        ArrayList<Substitution> curUnifier;

	    //Calculating the Unification for every rawZero
	    for (int i = 0; i < rawZeros.size(); i++) {
	        //Preparing the set of terms which should be unified
	        ArrayList<Integer> zeros = rawZeros.get(i);
            // reset term list
            terms = new ArrayList<>();

	        for (int j = 0; j < zeros.size(); j++) {
	            if(equ.termLefternSide[j]==null) continue;
	            for (int k = 0; k < zeros.get(j); k++) {
                    terms.add(equ.termLefternSide[j].clone());
	            }
	        }
	        try {
	            //be aware of an inequation
	            //So far it only works for an inequation 
	            //with the rightern side equal to zero
	            /*if(false && equ.inequation){
	                ArrayList<Substitution> emptyList = new ArrayList<>(0);
	                unifier.add(emptyList);
	            }
	            else{*/
                    curUnifier = recursiveUni(terms);
                    if(curUnifier.size() == 0) {
                      // only identifier is identity
                      curUnifier.add(new Substitution(true));
                    }
	                unifier.add(curUnifier);  
	            //}
            } catch (ArithmeticException e){
                //store an empty-unifier in the list
                Substitution emptySub = new Substitution();
                ArrayList<Substitution> emptyList = new ArrayList<>(0);
                emptyList.add(emptySub);
                unifier.add(emptyList);
                System.out.println(i+". No unifier for this raw zero.");
            }
        }
	    if(unifier.size()==0) {
	        System.out.println("There is no MGU for any zero.");
	    }
	    /**else{
	        System.out.println("We will return unifier:");
	        for (int i = 0; i < unifier.get(0).size(); i++) {
	            System.out.println(unifier.get(0).get(i).toString());
	        }
	    }**/
		return unifier;
	}

	/**
	 * 
	 * @param terms
	 * @return an ArrayList of Substitutions (a MGU)
	 * @throws ArithmeticException if Terms are not unifiable
	 */
	public ArrayList<Substitution> recursiveUni(ArrayList<Term> terms){
	    int amountTerm = terms.size();
	    int equalSymbols = 1;
	    ArrayList<Substitution> unifier = new ArrayList<>(0);
	    //if only one or zero terms unifier is trivial
	    if(amountTerm <= 1) return unifier;
	    Term var;
	    Term constant;
	    Boolean allDifferent = true;
	    do{
    	    for (int i = 1; i < amountTerm; i++) {
    	        //Both terms start with a different symbol?
    	        //System.out.println("these are the terms: "+terms.get(0).mySymbol+"   "+terms.get(i).mySymbol);
                if(terms.get(0).mySymbol.getName()!=terms.get(i).mySymbol.getName()){
                    //both terms start with a different constant or function?
                    if(!terms.get(0).mySymbol.isVariable() && !terms.get(i).mySymbol.isVariable()){
                        throw new ArithmeticException("nonunifiable");
                    }
                    else{
                        //Checking which one is the constant, which the variable
                        if(terms.get(0).mySymbol.isVariable()){
                            var = terms.get(0);
                            constant = terms.get(i);
                        }
                        else{
                            var = terms.get(i);
                            constant = terms.get(0);
                        }
                        if (var.contains(constant)) {
                                throw new ArithmeticException("nonunifiable");
                        }
                        else{
                            //new Substitution found
                            Substitution sub = new Substitution(var.mySymbol, constant);
                            unifier.add(sub);

                            for (int j = 0; j < terms.size(); j++) {
                                //Sadly we have to change the data type of the variable
                                //Term variable = new Term(sub.symbol);
                                Term changed = terms.get(j).replace(sub);
                                terms.set(j, changed);
                            }
                            unifier.addAll(recursiveUni(terms));
                            return unifier;
                        }
                    }
                }
                else {
                    equalSymbols++;
                }
            }
    	    //Every Term starts with the same symbol?
        	if (equalSymbols == amountTerm){
        	    //Are we at the recursion end?
        	    if(terms.get(0).toString().equals(terms.get(1).toString())){
        	        break;
        	    }
        	    //Is the symbol a constant?
        	    if (!terms.get(0).mySymbol.isVariable() && terms.get(0).mySymbol.getArity()==0) {
        	        return unifier;
                }
        	    //Is the symbol a Variable?
        	    else if (terms.get(0).mySymbol.isVariable()) {
                    return unifier;
                }
        	    //so it is a function
        	    //starting the recursion
        	    //so far does not work with functions which have the same name but differ in arity
        	    else{
        	        for (int i = 0; i < terms.get(0).children.length; i++) {
                        ArrayList<Term> childTerms = new ArrayList<>();
                        //seeking for every i^th childterm
                        for (int j = 0; j < amountTerm; j++) {
                            childTerms.add(terms.get(j).children[i]);
                        }
                        unifier.addAll(recursiveUni(childTerms));
                    }
        	    }
    	    }
        	for (int i = 1; i < amountTerm; i++) {
                if(terms.get(0).equals(terms.get(i))){
                    allDifferent = false;
                }
                else{
                    allDifferent = true;
                    break;
                }
            }
	    }while(allDifferent);
		return unifier;
	}
}
