package algebraicPetriNet;

/**
 * Represents a term, 
 * EXAMPLE: g(f(c),x)
 * (g is the symbol with arity two
 * f(c) and x are child terms.
 * 
 */
public class Term implements Cloneable {
	/**
	 * @param mySymbol Funcion, variable or constant.
	 * @param children If mysymbol is a function, children is the input of the function.
	 * If mysymbol is a variable or constant children is obsolete.
	 * NOTE: all children MUST BE NOT NULL
	 */
	public Term(Symbol mySymbol, Term... children) {
		this.mySymbol = mySymbol;
		assert(children.length == mySymbol.getArity());
		this.children = children;
	}

	protected Symbol mySymbol;
	protected boolean isVariable;
	protected Term[] children; // fixed array size, given by symbol arity

	/**
	 * @return A human readable version of the term.
	 */
	public String toString() {
		String s = "" + mySymbol.getName();

		if(mySymbol.getArity() > 0) {
			s = s + "( ";
			for(Term t : children) {
				s = s + t.toString();
				s = s + ", ";
			}
			s = s.substring(0, s.length()-2);
			s = s + " )";
		}
		return s;
	}

	/**
	 * 
	 * @param constant
	 * @return searches recursive in the term after the symbol of constant
	 */
    public boolean contains(Term constant) {
        boolean contains = false;
        if (this.mySymbol==constant.mySymbol){
            return true;
        }
        for (int i = 0; i < children.length; i++) {
            if(children[i].mySymbol==constant.mySymbol){
                return true;
            }
            else contains = children[i].contains(constant);
        }
        return contains;
    }

    @Override
    protected Term clone(){
        try {
            return (Term) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("You cannot clone");
            throw new InternalError();
        }
    }
    /**
     * 
     * @param sub
     * @return constructs a new Term, where all the specified variables are substituted
     * @throws CloneNotSupportedException 
     */
    public Term replace(Substitution sub){
            Term freshT = (Term) this.clone();
            freshT = innerReplace(sub);
            return freshT;
    }

    private Term innerReplace(Substitution sub) {
        //base case "constant"
        if((children == null) && (!isVariable)) return new Term(this.mySymbol);
        //recursive case "substitution"
        if(mySymbol.isVariable && this.mySymbol.getName() == sub.symbol.getName()){
            return new Term(sub.term.mySymbol, sub.term.children);
        }
        //base case "other variable"
        else if(this.mySymbol.isVariable){
            return new Term(this.mySymbol);
        }
        //recursive case "function"
        else{
            for (int i = 0; i < children.length; i++) {
                this.children[i].innerReplace(sub);
            }
            return this;
        }
    }
}