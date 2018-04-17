package algebraicPetriNet;

import java.util.ArrayList;
import java.util.List;

/**
 * A signature is a list of function symbols and variable symbols.
 */
public class Signature {
	private List<Symbol> symbols;
	// just for readable variables names
	private int variableCounter;
	private List<Symbol> variables;

	public Signature() {
		this.variableCounter=1;
		this.symbols= new ArrayList<Symbol>();
		this.variables= new ArrayList<Symbol>();
	}

	/**
	 * 
	 * @return Returns all the function symbols which are in the signature.
	 */
	public List<Symbol> getSymbols() {
		return symbols;
	}

	/**
	 * 
	 * @param symbol adds the specified function symbol to the signature
	 */
	public void addSymbol(Symbol symbol) {
		symbols.add(symbol);
	}

	/**
	 * 
	 * @param symbol A symbol which may be in the signature
	 * @return Returns true if the symbol is a function symbol in the signature, false otherwise.
	 */
	public boolean containsSymbol(Symbol symbol){
		return this.symbols.contains(symbol);
	}

	/**
	 * 
	 * @param symbol A symbol which may be in the signature
	 * @return Returns true if the symbol is a variable symbol in the signature, false otherwise.
	 */
	public boolean containsVariable(Symbol symbol){
		return this.variables.contains(symbol);
	}

	/**
	 * 
	 * @return A new variable which is now part of the signature.
	 */
	public Symbol getFreshVariable() {
		return getFreshVariable("x");
	}

	/**
	 * 
	 * @param prefix The variable will start with the prefix.
	 * @return A new variable which is now part of the signature.
	 */
	public Symbol getFreshVariable(String prefix) {
		Symbol s = new Symbol(prefix +(Integer.toString(variableCounter++)).toString(), (byte) 0, true);
		variables.add(s);
		return s;
	}

	/**
	 * 
	 * @return Human-readable version of the signature.
	 */
	public String toString() {
		String s = "";
		s = s + "function symbols:\n";
		for(Symbol symbol : this.symbols) {
			s = s + "  " + symbol.getName() + " / " + symbol.getArity() + "\n";
		}
		s = s + "\nvariable symbols:\n";
		for(Symbol symbol : this.variables) {
			s = s + "  " + symbol.getName() + "\n";
		}
		return s;
	}
}