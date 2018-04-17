package algebraicPetriNet;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides a parsing of - and working with an equation.
 * 
 *
 */
public class Equation{

	public int amount;
	public Term[] termLefternSide;
	public int[] amountLefternSide;
	public Term righternSide;
	public int right;
	//do we pars an inequation or an equation?
	public boolean inequation; 

	/**
	 * 
	 * @param numberOfPlaces Builds a new equation with numberOfPlaces variables.
	 * @param inequation Do we build an inequation or an equation?
	 */
	public Equation(int numberOfPlaces, boolean inequation) {
		this.amount=numberOfPlaces;
		this.termLefternSide = new Term[numberOfPlaces];
		this.amountLefternSide = new int[numberOfPlaces];
		this.righternSide = null;
		this.right=0;
		this.inequation = inequation;
	}

	/**
	 * 
	 * @param t term
	 * @param k factor
	 * @param place
	 */
	public void addTerm(Term t, int k, int place){
		termLefternSide[place] = t;
		amountLefternSide[place] = k;
	}

	/**
	 * 
	 * @param t term
	 * @param k factor
	 */
	public void addRighternSide(Term t, int k){
		this.righternSide=t;
		this.right=k;
	}

	/**
	 * 
	 * @param righternSide Equation may have an rightern side.
	 * Although by now we only work with homogenous equation,
	 * where the rightern side is zero
	 */
	public void setRighternSide(Term righternSide){
		this.righternSide=righternSide;
	}

	public Term copyOfTermLefternSide(Term t, int i) throws CloneNotSupportedException{
	    return termLefternSide[i].clone();
	}

	/**
	 * 
	 * @param n node should have the tag name "equation"
	 * @return the equation parsed from an xml (pnml) file.
	 */
	public static Equation parse(Node n){

		int counterOfPlaces=0;
		int amount=0;
		boolean negativeSign = false;

		NodeList summands = n.getChildNodes();
		Equation q = new Equation(summands.getLength(), false);
		if(n.getNodeName().equals("inequation")){
		      q = new Equation(summands.getLength(), true);
		}
		System.out.println("Parsing the equation:");
		for (int i = 0; i < summands.getLength(); i++) {
			if(summands.item(i)==null || summands.item(i).getNodeName()=="#text"){
				continue;
			}
			if(summands.item(i).getNodeName() == "subtrahend"){
				negativeSign=true;
			}
			Node current = summands.item(i).getFirstChild().getNextSibling();
			if(current == null){
				System.out.println("NextSibling returns null, maybe something is wrong with the spaces in the pnml-file?");
				continue;
			}

			amount= Integer.parseInt(current.getAttributes().getNamedItem("value").getTextContent());
			if(negativeSign==true){
				amount=amount*-1;
			}

			//Skipping the rightern side
			if (summands.item(i).getNodeName()=="result") {
				continue;
			}

			//Going back to parent node, then go to the child "term"
			current=summands.item(i).getChildNodes().item(3);

			Term term = parseterm(current);
			q.addTerm(term, amount, counterOfPlaces);
			System.out.println("Added the term: "+term.toString());
			counterOfPlaces++;
			negativeSign = false;
		}

		//TODO for later versions. Parse righternSide (for non-homogeneous equations)

		return q;
	}

	/**
	 * 
	 * @param node the current node
	 * @return the parsed term
	 */
	public static Term parseterm(Node node){
		//Provides correct working only for the examples.
		Symbol consSymbol = new Symbol("platzhalter", (byte) 0, false);
		Term consTerm = new Term(consSymbol);

		String var;
		NodeList children = node.getChildNodes();
		for(int i=0; i<children.getLength(); i++){
			Node current = children.item(i);
			if(current == null) continue;
			if(current.getNodeName() == "#text") continue;
			String next = current.getNodeName();

			switch (next) {

			case "arbitraryoperator":
				var = current.getAttributes().getNamedItem("id").getTextContent();
				Symbol funcSymbol = new Symbol(var, (byte) 1);
				return new Term(funcSymbol, parseterm(current.getFirstChild().getNextSibling()));

			case "variable":
				var = current.getAttributes().getNamedItem("refvariable").getTextContent();	
				Symbol variableSymbol = new Symbol(var, (byte) 0, true);
				System.out.println("");
				return new Term(variableSymbol);

			//case usersort is handled as a variable
			case "usersort":
				var = current.getAttributes().getNamedItem("declaration").getTextContent();
				Symbol constSymbol = new Symbol(var, (byte) 0, true);
				Term constTerm = new Term(constSymbol);
				return constTerm;

			case "input":
				return parseterm(current.getFirstChild().getNextSibling());

			case "output":

				break;	

			case "term":

				break;

			default:

				System.out.println("**********default case");
				break;
			}
		}
		return consTerm;		
	}	
}