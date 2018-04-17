package algebraicPetriNet;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * A Transition in an APN. It stores also the incoming
 * and outgoing arcs to every belonging place.
 *
 */
public class Transition {
	private final String name;
	private final String id;
	//one transition may have one transition-entry per arc
	private final Map<Place, TransitionEntry> pre;
	private final Map<Place, TransitionEntry> post;

	/**
	 * 
	 * @param name
	 * @param id for parsing
	 */
	public Transition(String name, String id) {
		this.name = name;
		this.id=id;
		this.pre = new HashMap<Place, TransitionEntry>();
		this.post = new HashMap<Place, TransitionEntry>();

	}

	/**
	 * 
	 * @param p
	 * @param e
	 */
	public void setPost(Place p, TransitionEntry e) {
		this.post.put(p, e);
	}

	/**
	 * 
	 * @param p
	 * @param e
	 */
	public void setPre(Place p, TransitionEntry e) {
		this.pre.put(p, e);
	}

	/**
	 * 
	 * @param p
	 */
	public void setPre(Place p) {
		this.setPre(p, new TransitionEntry());		
	}

	/**
	 * add a (term,weight)-pair for a specific preplace p
	 * @param p
	 * @param term
	 * @param weight
	 * 
	 * 
	 */
	public void addPre(Place p, Term term, int weight){
		this.pre.get(p).setTerm(term, weight);
	}

	/**
	 * 
	 * @param p
	 */
	public void setPost(Place p) {
		this.setPost(p, new TransitionEntry());
	}

	/**
	 * 
	 * @param p
	 * @param term
	 * @param weight
	 * 
	 * add a (term,weight)-pair for a specific postplace
	 */
	public void addPost(Place p, Term term, int weight){
		this.post.get(p).setTerm(term, weight);

	}

	/**
	 *  
	 * @return the name of Transition t
	 */
	public String getName(){
		return this.name;
	}

	/**
	 *  
	 * @return the id of Transition t
	 */
	public String getID(){
		return this.id;
	}
	@Override
	public String toString() {
		return "Transition " + name + "[pre: " + pre + ", post=" + post
				+ "]";
	}
}