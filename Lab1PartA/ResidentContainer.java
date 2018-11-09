package DeathAndTaxes;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;
/**
 * A collection for storing resident objects and retreiving them by any
 * property.
 * */
class ResidentContainer{
	private HashMap<String, Resident> resByLastName = new HashMap<>();
	private HashMap<String, Resident> resByAdress = new HashMap<>();
	private ArrayList<Resident> residents = new ArrayList<>();
	
	public ResidentContainer(){}
	public void add(Resident res){
		resByLastName.put(res.getLastName(), res);
		resByAdress.put(res.getAdress(), res);
		residents.add(res);
	}
	/**
	 * Returns a resident by name. If no resident with such name exists, a null
	 * refernce is returned instead.
	 * */
	public Resident getByLastName(String lastName){
		if(resByLastName.containsKey(lastName)){
			return resByLastName.get(lastName);
		} else {
			return null;
		}
	}
	/**
	 * Returns a resident by adress. If no resident with such adress exists, a
	 * null reference is returned instead.
	 * */
	public Resident getByAdress(String adress){
		if(resByAdress.containsKey(adress)){
			return resByAdress.get(adress);
		} else {
			return null;
		}
	}
	/**
	 * Iterates over each resident and applies function object's method to each
	 * resident sequentially.
	 * */
	public void forEach(ResidentFunc func){
		for(int i = 0; i < residents.size(); i++){
			func.run(residents.get(i));
		}
	}
	/**
	 * Sorts the collection with the specified comparator.
	 * */
	public void sort(Comparator<Resident> cmp){
		residents.sort(cmp);
	}
	/**
	 * Returns all residents which satisfy specified predicate.
	 * */
	public ArrayList<Resident> filter(Predicate<Resident> pr){
		ArrayList<Resident> ret = new ArrayList<Resident>();
		for(int i = 0; i < residents.size(); i++){
			if(pr.test(residents.get(i))) ret.add(residents.get(i));
		}
		return ret;
	}
	/**
	 * Returns the amount of residents in the collection.
	 * */
	public int size(){
		return residents.size();
	}
}
