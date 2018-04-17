package algebraicPetriNet;

/**
 * A Symbol represents a function symbol or a variable symbol of a signature.
 * E.g. in the term "f(g)", "f" is a unary function symbol.
 * 
 */
public class Symbol {
	private final String name;
	private final byte arity;
	public final Boolean isVariable;

	/** 
	 * @param name: e.g. "f" as in the term "f(x)"
	 * @param arity: e.g. 2 as for "f(x,y)"
	 */
	public Symbol(String name, byte arity) {
		this.name = name;
		this.arity = arity;
		this.isVariable = false;
	}

	/**
	 * 
	 * @param name e.g. "f" as in the term "f(x)"
	 * @param arity e.g. 2 (or very high) as for "f(x,y)"
	 */
	public Symbol(String name, int arity) {
		this.name = name;
		this.arity = (byte) arity;
		this.isVariable = false;
	}

	/**
	 * 
	 * @param name name of the symbol
	 * @param arity should be zero
	 * @param isVariable true, if it is a variable, false if it is a constant.
	 */
	public Symbol(String name, Byte arity, boolean isVariable) {
		this.name = name;
		this.arity = arity;
		this.isVariable = isVariable;
	}

	/**
	 * 
	 * @return the name of the symbol
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the arity of the symbol
	 */
	public Byte getArity() {
		assert(arity >= 0);
		return arity;
	}

	public Boolean isVariable(){
	    return this.isVariable;
	}
}