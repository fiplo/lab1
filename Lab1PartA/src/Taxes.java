package DeathAndTaxes;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Predicate;

/**
 * Main class of the program that performs calculations specified in the task.
 *
 * <p>In its current state this class can accomodate two lists of command line 
 * arguments, for utility and resident files respectively. Lists are separated 
 * by a ';' literal.</p>
 *
 * <b>Task:</b>
 * <ol>
 *   <li>Sort residents by name.</li>
 *   <li>Print utilities, residents and their respective payments.</li>
 *   <li>Print residents who paid less than average.</li>
 *   <li>Print which utility was cheapest which month.</li>
 * <ol>
 * */
public class Taxes{
	private UtilityContainer utilities = new UtilityContainer();
	private ResidentContainer residents = new ResidentContainer();

	/**
	 * Takes a two lists of file names containing relevant data and performs
	 * calculations specified in the task.
	 *
	 * <p>Lists of filenames are separated by a literal semi-colon.</p>
	 * */
	public static void main(String[] args){
		Taxes uselessObject = new Taxes();
		uselessObject.run(args);
	}
	private void run(String[] args){
		readFiles(args);
		//Sorting and printing formatted output with nested lambdas.
		residents.sort((Resident x, Resident y) -> 
				x.getLastName().compareTo(y.getLastName()));
		residents.forEach(x -> {
				System.out.format("%1$-20s \n", x.getLastName());
				x.forEachPayment(y -> {
					System.out.format("\t%1$-20s %2$-3d %3$-5d %4$-3d\n", 
						  y.utilityPaidFor.name
						, y.month
						, y.amount
						, y.getTotalCost());
				});
			});
		ArrayList<Resident> paidLAvg // Residents who paid less than average
			= residents.filter(new ResidentPayPredicate(getAverageMoneySpent()));
		System.out.println("Residents who paid less than average:");
		for(int i = 0; i < paidLAvg.size(); i++){
			System.out.println("\t" + paidLAvg.get(i).getLastName());
		}
		MonthUtilityPair mntUt = getCheapestUtility();
		System.out.println("Cheapest utility:\n\tName:" + mntUt.utilPaidFor.name 
				+ "\n\tMonth: " + mntUt.month);
	}
	private void readFiles(String[] fileNames){
		int i = 0;
		while(i < fileNames.length && !fileNames[i].equals(";")){
			readUtilFile(fileNames[i]);
			i++;
		}
		i++;
		while(i < fileNames.length){
			readResidentFile(fileNames[i]);
			i++;
		}
	}
	private void readUtilFile(String fileName){
		UtilityFileReader utilParser = new UtilityFileReader();
		Utility[] utilArr = utilParser.parseUtilityFile(fileName);
		if(utilArr == null){ 
			whine("Utility array is null. Fix your shit");
			System.exit(-1);
		}
		mapUtilities(utilArr);
	}
	private void readResidentFile(String fileName){
		Resident[] residentArr 
			= new ResidentFileReader(utilities).parseResidentFile(fileName);
		if(residentArr == null){ 
			whine("Resident array is null. Fix your shit.");
			return;
		}
		mapResidents(residentArr);
	}
	private static final void whine(String txt){
		if(txt == null) return;
		System.err.println(txt);
	}
	private void mapUtilities(Utility[] arr){
		if(arr == null){
			whine("Attempted to map null utility array.");
			return;
		}
		for(int i = 0; i < arr.length; i++){
			utilities.add(arr[i]);
		}
	}
	private void mapResidents(Resident[] arr){
		if(arr == null){
			whine("Attempted to map null resident array.");
			return;
		}
		for(int i = 0; i < arr.length; i++){
			residents.add(arr[i]);
		}
	}
	private long getAverageMoneySpent(){
		return getTotalMoneySpent() / residents.size();
	}
	private long getTotalMoneySpent(){
		ResidentSummator resSum = new ResidentSummator();
		residents.forEach(resSum);
		return resSum.getSum();
	}
	private MonthUtilityPair getCheapestUtility(){
		MonthUtilitySummator summ = new MonthUtilitySummator(utilities);
		residents.forEach(summ);
		return summ.getCheapestMonthUtil();
	}
	private static class MonthUtilityPair{
		public final int month;
		public final Utility utilPaidFor;

		public MonthUtilityPair(int month, Utility utilPaidFor){
			this.month = month;
			this.utilPaidFor = utilPaidFor;
		}
	}
	private static class MonthUtilitySummator 
			implements ResidentFunc, PaymentFunc{
		private HashMap<Utility, MonthlySum> monthUtilitySums = new HashMap<>();

		public MonthUtilitySummator(UtilityContainer utils){
			Utility[] utilArr = utils.getUniqueUtils();
			for(int i = 0; i < utilArr.length; i++){
				monthUtilitySums.put(utilArr[i], new MonthlySum()); 
			}
		}
		public MonthUtilityPair getCheapestMonthUtil(){
			Utility minUtil = null;
			int minSum = Integer.MAX_VALUE;
			int minMonth = -1;
			for(Map.Entry<Utility, MonthlySum> keyValPair 
					: monthUtilitySums.entrySet()){
				MonthlySum.MonthAmountPaidPair crrSum = keyValPair.getValue().getMin();
				if(crrSum.amount < minSum && crrSum.amount != 0){
					minUtil = keyValPair.getKey();
					minSum = crrSum.amount;
					minMonth = crrSum.month;
				}
			}
			return new MonthUtilityPair(minMonth, minUtil);
		}
		//Read as "For each resident"
		public void run(Resident res){
			res.forEachPayment(this);
		}
		//Read as "for each resident's payments".
		//Does this as each payment is passed to it.
		public void run(Payment pmt){
			MonthlySum toAddTo = monthUtilitySums.get(pmt.utilityPaidFor);
			toAddTo.add(pmt.month, pmt.getTotalCost());
		}
		private static class MonthlySum{
			private int[] arr = new int[12];
			public void add(int month, int amount){
				//Could throw my own exception here but
				//java runtime will do that for me anyway.
				arr[month - 1] += amount;
			}
			public MonthAmountPaidPair getMin(){
				int amount = Integer.MAX_VALUE;
				int month = -1;
				for(int i = 0; i < 12; i++){
					if(arr[i] < amount && arr[i] != 0){
						amount = arr[i];
						month = i + 1;
					}
				}
				if(month == -1) return new MonthAmountPaidPair(-1, Integer.MAX_VALUE);
				return new MonthAmountPaidPair(month, amount);
			}
			public static class MonthAmountPaidPair{
				public final int month;
				public final int amount;

				public MonthAmountPaidPair(int month, int amount){
					this.month = month;
					this.amount = amount;
				}
			}
		}
	}
	private static class ResidentPayPredicate implements Predicate<Resident>{
		private long avgAmountPaid;
		private ResidentSummator summ = new ResidentSummator();

		public ResidentPayPredicate(long avgAmountPaid){
			this.avgAmountPaid = avgAmountPaid;
		}
		public boolean test(Resident r){
			summ.reset();
			summ.run(r);
			return summ.getSum() < avgAmountPaid;
		}
	}
	private static class ResidentSummator implements ResidentFunc{
		public PaymentSummator paySummator = new PaymentSummator();
		public void run(Resident res){
			res.forEachPayment(paySummator);
		}
		public long getSum(){return paySummator.getSum();}
		public void reset(){paySummator.reset();}
	}
	private static class PaymentSummator implements PaymentFunc{
		private long sum = 0;
		public void run(Payment arg){
			sum += arg.getTotalCost();
		}
		public long getSum(){
			return sum;
		}
		public void reset(){
			sum = 0;
		}
	}
}
