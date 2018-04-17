package algebraicPetriNet;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * A place in an APN which may contain token.
 *
 */
public class Place {
	public final String name;
	public final String id;
	//places contain tokens (<specific token, amount>)
	public Map<String, Integer> allToken = new HashMap<String, Integer>();
	private static Map<String,Place> placeByName = new HashMap<String,Place>();

	/**
	 * 
	 * @param name name of the place
	 * @param id for parsing
	 */
	public Place(String name, String id) {
		this.id=id;
		this.name = name;
		Place.placeByName.put(name, this);
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * 
	 * @return name of the place
	 */
	public String getName(){
		return this.name;
	}

	/**
	 * 
	 * @return id of the place
	 */
	public String getID(){
		return this.id;
	}
	/**
	 * 
	 * @param n
	 * @return the belonging place
	 */
	public static Place getPlaceByName(String n) {
		return Place.placeByName.get(n);
	}
}