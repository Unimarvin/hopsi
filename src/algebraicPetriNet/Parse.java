package algebraicPetriNet;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
/**
 * 
 * Provides the parsing of the arcs of an APN.
 *
 */
public class Parse {
	/**
	 * This method may parse the term of an arc. The term may consist of an (weighted) variable,
	 * a constant, or a function, which must have one input and one output.
	 * 
	 * @param s for intern use
	 * @param n current node
	 * @param weight current weight of arc; initialize with 1
	 * @param arity arity of the current function
	 * 
	 * 
	 */
	public static void arcs(String s, Node n, TransitionEntry tEntry, int weight, int arity){
		String var;
		NodeList children = n.getChildNodes();

		for(int i=0; i<children.getLength(); i++){
			Node current = children.item(i);
			if(current == null) continue;
			if(current.getNodeName() == "#text") continue;
			String next = current.getNodeName();

			switch (next) {
			case "constant":
				var = current.getAttributes().getNamedItem("refconstant").getTextContent();
				Symbol constSymbol = new Symbol(var, (byte) 0, false);
				Term constTerm = new Term(constSymbol);
				tEntry.setTerm(constTerm, weight);
				System.out.println("Created the transition entry: "+ tEntry.toString()+".");
				System.out.println("");
				arcs(next, current, tEntry, weight, arity);
				break;

			case "weight":
				String yeah = ((Element) current).getAttributes().getNamedItem("numberconstant").getTextContent();
				weight = Integer.parseInt(yeah);
				break;
				
			case "arbitraryoperator":
				//By now only functions with single variables
			    
				var = current.getFirstChild().getNextSibling().getFirstChild().getNextSibling().getAttributes().getNamedItem("declaration").getTextContent();
				Symbol funcInputSymbol = new Symbol(var, (byte) 0);

				var = current.getAttributes().getNamedItem("id").getTextContent();
				Symbol funcSymbol = new Symbol(var, (byte) 1); 
				Term funcInputTerm = new Term(funcInputSymbol);
				Term funcTerm = new Term(funcSymbol, funcInputTerm);
				tEntry.setTerm(funcTerm, weight);
				System.out.println("Created the transition entry: "+ tEntry.toString()+".");
				System.out.println("");
				arcs(next, current, tEntry, weight, arity);

				break;

			case "variable":
				var = current.getAttributes().getNamedItem("refvariable").getTextContent();
				Symbol variableSymbol = new Symbol(var, (byte) 0, true);
				Term variableTerm = new Term(variableSymbol);
				tEntry.setTerm(variableTerm, weight);
				System.out.println("Created the transition entry: "+ tEntry.toString()+".");
				System.out.println("");
				arcs(next, current, tEntry, weight, arity);
				break;

			default:
				arcs(next,current, tEntry, weight, arity);
				break;
			}
		}
	}

	/**
	 * This method is not used so far. 
	 * 
	 * @param base The node in the xml-tree where the method starts to search
	 * @param signature The signature of the APN
	 */
	public static void signature(Element base, Signature signature){
		//Parsing the sort
		//By now considering only one-sorted terms
		//TODO for later versions. How to save the sort in our data-structure

		//parsing the constants
		NodeList constNodes = base.getElementsByTagName("feconstant");
		String[] namesConstants = new String[constNodes.getLength()];
		System.out.println("constNodes has "+constNodes.getLength()+" elements.");
		for (int i = 0; i < constNodes.getLength(); i++) {
			namesConstants[i] = constNodes.item(i).getAttributes().getNamedItem("id").getTextContent();
			System.out.println("There is constant "+ namesConstants[i]);
			signature.addSymbol(new Symbol(namesConstants[i], (byte) 0, false));
		}

		//parsing the variables
		NodeList variableNodes = base.getElementsByTagName("variabledecl");
		String[] namesvariables = new String[variableNodes.getLength()];
		for (int i = 0; i < variableNodes.getLength(); i++) {
			namesvariables[i] = variableNodes.item(i).getAttributes().getNamedItem("id").getTextContent();
			System.out.println("There is variable "+ namesvariables[i]);
			signature.addSymbol(new Symbol(namesvariables[i], (byte) 0, true));
		}
	}
}