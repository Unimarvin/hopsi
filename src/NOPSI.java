import algebraicPetriNet.*;

// for parsing XML file
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class NOPSI {

	/**
	 * 
	 * @param args First parameter must be the filename of an .pnml-file.
	 */
	public static void main(String[] args) {
        boolean nopsiQuiet = false;

		//Robustness: NOPSI is checking the given parameter, which should be the .pnml APN.
		if(args.length==0){
			System.out.println("Error: Please give the .pnml net which shoul be parsed as a parameter.");
			return;
		}
		if(args.length>2){
			System.out.println("Error: Too many parameters.");
			return;
		}

        // check for quiet parameter
        if(args.length == 2 && (args[0] == "-q" || args[0] == "quiet")) {
          args[0] = args[1];
          nopsiQuiet = true;
        }
        if(args.length == 2 && (args[1].equals("-q") || args[1].equals("quiet"))) {
          System.out.println("nopsi quiet");
          nopsiQuiet = true;
        }

		if(!args[0].endsWith(".pnml")){
			System.out.println("Error: Please make sure that the given argument is a .pnml-file.");
			return;
		}

        if(!nopsiQuiet) {
    	  System.out.println("Hi, I'm NOPSI.");
	      System.out.println("I am helping to compute NOn-Preserving Steps of Inequalities.");
		  System.out.println("");
        }

		//needed variables
		int numberOfPlaces, numberOfTransitions;
		try {
			File file = new File(args[0]);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			try {
				db = dbf.newDocumentBuilder();
				Document document = db.parse(file);
				Element base = document.getDocumentElement();
				if(!nopsiQuiet) System.out.println("Parsing the Algebraic Petri net:");

				//Parsing the places.
				//get all the nodes by tag name "place"
				NodeList placeNodes = base.getElementsByTagName("place");
				//get the number of places
				numberOfPlaces=placeNodes.getLength();
				//list for our places
				Place[] listOfPlaces = new Place[numberOfPlaces]; 
				for(int i=0; i<placeNodes.getLength(); i++){
					Node current = placeNodes.item(i);
					String id = current.getAttributes().getNamedItem("id").getTextContent();
					NodeList placeChildren = current.getChildNodes();
					for (int j=0; j<placeChildren.getLength(); j++) {
						Node currentPlaceChild = placeChildren.item(j);
						if(currentPlaceChild.getNodeName() == "name") {
							Node placeNameNode = currentPlaceChild.getFirstChild().getNextSibling();
							String placeName = placeNameNode.getTextContent();
							// storing the place in our data-construct
							listOfPlaces[i]= new Place(placeName, id);
							int number=i+1;
							if(!nopsiQuiet) System.out.println(number+". Place with name " + placeName+" and id "+id+" created.");
						}
					}
				}
				if(!nopsiQuiet) System.out.println("");
				//Testing if two places have the same name.
				for (int i= 0; i < listOfPlaces.length; i++) {
					for (int j = i+1; j < listOfPlaces.length; j++) {
						if(listOfPlaces[i].getName().equals(listOfPlaces[j].getName())){
							System.out.println("Error: The place: "+listOfPlaces[i].getName()+" is doubled.");
							return;
						}
					}
				}

                /*
				//Parsing the transitions.
                // CURRENTLY DISABLED AS UNUSED

				NodeList transitionNodes = base.getElementsByTagName("transition");
				//get the number of transitions
				numberOfTransitions=transitionNodes.getLength();
				//list for our transitions
				Transition[] listOfTransitions = new Transition[numberOfTransitions];
				for(int i=0; i<numberOfTransitions; i++){
					Node current = transitionNodes.item(i);
					String idTransition = current.getAttributes().getNamedItem("id").getTextContent();
					NodeList transitionChildren = current.getChildNodes();
					for (int j=0; j<transitionChildren.getLength(); j++) {
						Node currentTransitionChild = transitionChildren.item(j);
						if(currentTransitionChild.getNodeName() == "name") {
							Node transitionNameNode = currentTransitionChild.getFirstChild().getNextSibling();
							String transitionName = transitionNameNode.getTextContent();
							// storing the place in our data-construct
							listOfTransitions[i]= new Transition(transitionName, idTransition);
							int number=i+1;
							System.out.println(number+". Transition with name " +transitionName+" and id "+idTransition+" created.");						}
					}
				}
				System.out.println("");
                */

                /*
				//Parsing arcs.
                // CURRENTLY DISABLED AS UNUSED
				NodeList arcNodes = base.getElementsByTagName("arc");
				int arcsAmount = arcNodes.getLength();

				for (int i = 0; i < arcsAmount; i++) {
					String curSource = ((Element) arcNodes.item(i)).getAttributes().getNamedItem("source").getTextContent();
					String curTarget = ((Element) arcNodes.item(i)).getAttributes().getNamedItem("target").getTextContent();
					//checks if source of the arc is a transition or a place 
					//and checks which place/transition is the target
					for (int j = 0; j < numberOfTransitions; j++) {
						if(listOfTransitions[j].getID().equals(curSource)){
							for (int j2 = 0; j2 < numberOfPlaces; j2++) {
								if(listOfPlaces[j2].getID().equals(curTarget)){
									listOfTransitions[j].setPost(listOfPlaces[j2]);	
									System.out.println("Arc from transition "+ listOfTransitions[j].getName() +" to place " +listOfPlaces[j2].getName()+" created.");
									//Parsing the terms of the arcs.
									//Correct working of the function is only guaranteed for those 4 examples
									TransitionEntry tEntry = new TransitionEntry(true);
									Parse.arcs("structure", arcNodes.item(i), tEntry, 1, 0);
								}
							}
						}
						else if(listOfTransitions[j].getID().equals(curTarget)){
							for (int j2 = 0; j2 < numberOfPlaces; j2++) {
								if(listOfPlaces[j2].getID().equals(curSource)){
									listOfTransitions[j].setPre(listOfPlaces[j2]);
									System.out.println("Arc from place " +listOfPlaces[j2].getName()+ " to transition "+ listOfTransitions[j].getName()+" created.");
									TransitionEntry tEntry = new TransitionEntry(false);
									Parse.arcs("structure", arcNodes.item(i), tEntry, 1, 0);
								}
							}
						}
					}
				}
                */

				//Parsing equation. (or inequation)
				NodeList equ = base.getElementsByTagName("equation");
				if(equ.getLength()==0){
	                equ = base.getElementsByTagName("inequation");
				}

				System.out.println("");
				Equation equation = Equation.parse(equ.item(0));				

				//NOPSI.java gets an ArrayList with tuples, where all zeros are stored
				ArrayList<ArrayList<Integer>> rawZeros = Zero.getAll(equation, numberOfPlaces);
				if(!nopsiQuiet) {
                  System.out.println("These are the possible solutions:");
	              for (int i = 0; i < rawZeros.size(); i++) {
		  		    System.out.print(i+". Array: ");
					  for (int j = 0; j < rawZeros.get(i).size(); j++) {
						  System.out.print(rawZeros.get(i).get(j)+"|");
					  }
					  System.out.println("");
				  }
                }

				//Checking if there are no zeros
				if(rawZeros.size()==0){
					System.out.println("There are no zeros.");
				} else {
                    System.out.println("There are " + rawZeros.size() + " abstract zeros.");
    				Logic logic = new Logic();
    				ArrayList<ArrayList<Substitution>> allSubstitutions = logic.unification(equation, rawZeros);
   
      		        if(allSubstitutions.size()>0){
                       if(!nopsiQuiet) {
    				      System.out.println("These are the MGUs:");
                          for (int i = 0; i < allSubstitutions.size(); i++) {
                              System.out.print(i+". ");
                              for (int j = 0; j < allSubstitutions.get(i).size(); j++) {
                                  System.out.print(allSubstitutions.get(i).get(j).toString());
                                  if(j < allSubstitutions.get(i).size()-1) System.out.print(", ");
                              }
                              System.out.println("");
                          } 
    				  }
                   }
				}
			} catch (FileNotFoundException e) {
				System.out.println("Error: Your given file could not be found.");
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// DONE Parse inequality file..
		// store as data structure / similar to transition (term-weight tuples)

		// DONE compute set of one-term zeros
		   // variant 1: DONE iterate to bound
		   // variant 2: linear optimization
		   // variant 3: smt
		// TODO for later versions. compute set of derivations
		// TODO for later versions. compute non-preserving steps
	}
}
