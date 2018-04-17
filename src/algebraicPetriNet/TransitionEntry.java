package algebraicPetriNet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * A TransitionEntry describes the (term,weight)-pair of an arc,
 * which goes into, or comes out of a transition.
 * One arc may have even multiple (term,weight)-pairs.
 *
 */
public class TransitionEntry {
	//per transition and per place is a map of multiple terms and their belonging weights
	//HOWEVER for our examples there are only one (term, weight)-pair for every transition and its place needed.
	public Map<Term, Integer> allTerms;
	public Place p;
	public Boolean outgoing;

	/**
	 * 
	 */
	public TransitionEntry(){
		this.allTerms= new  LinkedHashMap<Term, Integer>();
	}

	/**
	 * 
	 * @param outgoing set to true if arc from transition to place, false otherwise
	 */
	public TransitionEntry(Boolean outgoing){
		this.allTerms= new  LinkedHashMap<Term, Integer>();
		this.outgoing=outgoing;
	}

	/**
	 * Adds a new (term,weight)-pair, or increases the weight, if the term is already set.
	 * @param term the term which should be put
	 * @param weight the belonging weight
	 */
	public void addTermweight(Term term, int weight){
		int current=0;
		if(this.allTerms.containsKey(term)){
			current = this.allTerms.get(term);
		}
		this.allTerms.put(term, current+weight);
	}  

	/**
	 * 
	 * @param term the term which should be set
	 * @param weight the belonging weight
	 */
	public void setTerm(Term term, int weight){
		if(!this.allTerms.containsKey(term)){
			this.allTerms.put(term, weight);
		}
		else{
			System.out.println("term is already set");
		}
	}

	@Override
	public String toString() {
		return this.allTerms.toString();
	}
}