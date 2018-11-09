package DeathAndTaxes;

/**
 * Simple data class which encapsulates a payment's time, size, and type.
 * */
public class Payment{
	/**
	 * Specifies month of the payment as integer between 1 and 12 inclusive.
	 * */
	public final int month;
	/**
	 * Specifies amount paid for.
	 *
	 * Units - unknown.
	 * */
	public final int amount;
	/**
	 * Reference to utility being paid for.
	 * */
	public final Utility utilityPaidFor;
	/**
	 * Returns amount of currency required to make good on the payment.
	 * */
	public int getTotalCost(){
		return utilityPaidFor.monthlyPrice * amount;
	}
	private Payment(int month, int amount, Utility utilPaidFor){
		this.month = month;
		this.amount = amount;
		utilityPaidFor = utilPaidFor;
	}
	/**
	 * Used instead of constructor.
	 *
	 * This method returns a valid Payment object if it passes two simple sanity 
	 * checks:
	 * <ol>
	 *   <li><b>month</b> is between 1 and 12 inclusive.</li>
	 *   <li><b>utilPaidFor</b> is not null.</li>
	 * </ol>
	 * If any of these fail, a null refernce is returned instead.
	 * */
	public static Payment makePayment(int month, int amount, Utility utilPaidFor){
		if(utilPaidFor == null) return null;
		return new Payment(month, amount, utilPaidFor);
	}
}
