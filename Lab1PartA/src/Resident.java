package DeathAndTaxes;

import java.util.ArrayList;

/**
 * Data class reperesenting a resident which encapsulates resident's last name,
 * adress and payments.
 * */
public class Resident{
	private String lastName;
	private String adress;
	private ArrayList<Payment> payments;

	public String getLastName(){return lastName;}
	public String getAdress(){return adress;}
	public Payment getPayment(int index){return payments.get(index);}
	public int getNumberOfPayments(){return payments.size();}
	public Resident(String lastName, String adress, Payment[] payments){
		this.lastName = lastName;
		this.adress = adress;
		this.payments = new ArrayList<Payment>();
		for(int i = 0; i < payments.length; i++){
			this.payments.add(payments[i]);
		}
	}
	public Resident(String lastName, String adress){
		this.lastName = lastName;
		this.adress = adress;
		payments = new ArrayList<Payment>();
	}
	/**
	 * Iterates over resident's payments and applies function object's method to 
	 * each payment.
	 *
	 * @param func Function object that operates on payments.
	 * */
	public void forEachPayment(PaymentFunc func){
		for(int i = 0; i < payments.size(); i++){
			func.run(payments.get(i));
		}
	}
	/**
	 * Adds a single payments to residents payment list.
	 *
	 * Almost guaranteed to succeed. Does not accept null references. Besides 
	 * that the only way this can fail is an OutOfMemoryException but if this 
	 * gets thrown, you really can't do anything at that point anyway.
	 * */
	public void addPayment(Payment pmt){
		payments.add(pmt);
	}
}
