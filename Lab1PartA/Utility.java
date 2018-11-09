package DeathAndTaxes;
/**
 * Dumb data class that describes an utility.
 *
 * Objects of this class are immutable.
 * */
public class Utility{
	public final int code;
	public final String name;
	public final int monthlyPrice;

	public Utility(int code, String name, int monthlyPrice){
		this.code = code;
		this.name = name;
		this.monthlyPrice = monthlyPrice;
	}
}
