package DeathAndTaxes;

import java.util.HashMap;
import java.util.ArrayList;
/**
 * Contains methods for coveniently storing an retreving utility objects.
 * */
class UtilityContainer{
	private HashMap<Integer, Utility> utilByCode = new HashMap<>();
	private HashMap<String, Utility> utilByName = new HashMap<>();
	//util is used for things which cannot be stored in a HashMap
	//because the particular property is not guaranteed to be unique.
	private ArrayList<Utility> util = new ArrayList<>();

	public UtilityContainer(){}
	/**
	 * Adds a single utility to the collection.
	 *
	 * Name and code of an utility must be unique if it is to be added to this
	 * collection.
	 * */
	public void add(Utility util){
		utilByCode.put(util.code, util);
		this.util.add(util);
		utilByName.put(util.name, util);
	}
	/**
	 * Returns a utility which has the same code as argument, if such exists.
	 *
	 * A null reference is returned otherwise.
	 * */
	public Utility getByCode(int code){
		if(utilByCode.containsKey(new Integer(code))){
			return utilByCode.get(new Integer(code));
		} else {
			return null;
		}
	}
	/**
	 * Returns all utilities which have the same price as the argument.
	 * */
	public ArrayList<Utility> getByPrice(int price){
		ArrayList<Utility> ret = new ArrayList<>();
		for(int i = 0; i < util.size(); i++){
			if(util.get(i).monthlyPrice == price){
				ret.add(util.get(i));
			}
		}
		return ret;
	}
	/**
	 * Returns a utility which has the same name as the argument.
	 *
	 * If no such object exists, a null reference is returned instead.
	 * */
	public Utility getByName(String name){
		if(utilByName.containsKey(name)){
			return utilByName.get(name);
		} else {
			return null;
		}
	}
	/**
	 * Returns all utilities in this collection as an array.
	 * */
	public Utility[] getUniqueUtils(){
		return util.toArray(new Utility[0]);
	}
}
