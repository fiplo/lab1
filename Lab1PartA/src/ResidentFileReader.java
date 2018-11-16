package DeathAndTaxes;

import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

/**
 *  Parses files which describe a collection of residents.
 *
 *  This class parses files of the following format:
 *  <ul>
 *	  <li>file 		= resident*</li>
 *	  <li>resident 	= lastName ',' adress payment+</li>
 *	  <li>payment 	= ',' month ',' code ',' amount</li>
 *	  <li>lastName 	= alphaNum</li>
 *	  <li>adress 	= alphaNum (Can be multiple words if in quotes)</li>
 *	  <li>month 	= integer</li>
 *	  <li>code		= integer</li>
 *	  <li>amount	= number</li>
 *	</ul>
 *
 *	Example:
 *		[1]Green, [2]"Lincoln st. 41", [3]2, [4]20341, [5]20
 *
 *		1: Last name
 *		2: Adress
 *		3: Month of first payment
 *		4: Code of utility
 *		5: Amount of <something> used
 *		Repeat 3, 4, 5 as much as needed.
 *
 *	White space is insignificant when parsing.
 */
public class ResidentFileReader{
	private Token[] tokens;
	private int crrPos;
	private UtilityContainer utilities;
	private ArrayList<Resident> residents = new ArrayList<>();

	public static void main(String[] args){
		UtilityFileReader helper = new UtilityFileReader();
		UtilityContainer utils = new UtilityContainer();
		int crrArg = 0;
		while(crrArg < args.length){
			if(args[crrArg].equals(";")){
				crrArg++;
				break;
			}
			Utility[] parsedUtils = helper.parseUtilityFile(args[crrArg]);
			for(int i = 0; i < parsedUtils.length; i++){
				utils.add(parsedUtils[i]);
			}
			crrArg++;
		}
		ResidentFileReader self = new ResidentFileReader(utils);
		ArrayList<Resident> ret = new ArrayList<>();
		while(crrArg < args.length){
			Resident[] res = self.parseResidentFile(args[crrArg]);
			for(int i = 0; i < res.length; i++){
				ret.add(res[i]);
			}
			crrArg++;
		}
		for(int i = 0; i < ret.size(); i++){
			Resident rtp = ret.get(i);
			//Very long lines. I am very sorry for you loss.
			System.out.println("Resident #" + i + ":\n\tLast name: " + rtp.getLastName() + "\n\tAdress: " + rtp.getAdress() + "\n\t Payments:");
			rtp.forEachPayment(x -> System.out.println("\t\tMonth: " + x.month + "\n\t\tAmount: " + x.amount + "\n\t\tUtility: " + x.utilityPaidFor.name + "\n"));
		}
	}

	public ResidentFileReader(UtilityContainer util){
		utilities = util;
	}
	private Token crrToken(){
		return tokens[crrPos];
	}
	private boolean EOS(){
		return crrPos >= tokens.length;
	}
	public Resident[] parseResidentFile(String fileName){
		try{
			Tokeniser tkn = new Tokeniser();
			String txt = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
			tokens = tkn.tokenise(txt);
			crrPos = 0;
			while(!EOS()){
				Resident res = parseResident();
				if(res == null) return null;
				residents.add(res);
				if(EOS()) break;
			}
			Resident[] ret = new Resident[residents.size()];
			residents.toArray(ret);
			return ret;
		} catch (Exception ex){
			return null;
		}
	}
	//lastName ',' adress payment+ ';'
	public Resident parseResident(){
		//Input validation
		int oldPos = crrPos;
		if(tokens.length - crrPos < 3) return null;
		boolean isValid = tokens[crrPos].type == TokenType.AlphaNumeric 
				|| tokens[crrPos].type == TokenType.QuotedString;
		isValid &= tokens[crrPos + 1].str.equals(",");
		isValid &= tokens[crrPos + 2].type == TokenType.AlphaNumeric 
			|| tokens[crrPos + 2].type == TokenType.QuotedString;
		if(!isValid) return null;
		//Reading data
		String lastName = crrToken().str;
		crrPos += 2;	//Skipping over comma
		String adress = crrToken().str;
		crrPos++;
		Resident rsd = new Resident(lastName, adress);
		while(!EOS()){
			Payment pmt = parsePayment();
			if(pmt == null) break;
			rsd.addPayment(pmt);
		}
		if(!crrToken().str.equals(";")){
			crrPos = oldPos;
			return null;
		}
		crrPos++;
		return rsd;
	}
	public Payment parsePayment(){
		int oldPos = crrPos;
		//Input validation
		if(tokens.length - crrPos < 6) return null;
		boolean isValid = tokens[crrPos].str.equals(",");
		isValid &= isInteger(tokens[crrPos + 1]);
		isValid &= tokens[crrPos + 2].str.equals(",");
		isValid &= isInteger(tokens[crrPos + 3]);
		isValid &= tokens[crrPos + 4].str.equals(",");
		isValid &= tokens[crrPos + 5].type == TokenType.Number;
		if(!isValid) return null;
		//Reading data
		crrPos++;
		int month = Integer.parseInt(crrToken().str);
		if(month < 1 || month > 12){
			System.err.println("Failed to parse payment. Month must be between 1 and 12");
			return null;
		}
		crrPos += 2;
		int code = Integer.parseInt(crrToken().str);
		if(utilities.getByCode(code) == null){
			System.err.println("Failed to parse payment. There is no utility with code " + code + ".");
			crrPos = oldPos;
			return null;
		}
		crrPos += 2;
		int amount = Integer.parseInt(crrToken().str);
		crrPos++;
		return Payment.makePayment(month, amount, utilities.getByCode(code));
	}
	private static final boolean isInteger(Token tkn){
		if(tkn.type != TokenType.Number) return false;
		for(int i = 0; i < tkn.str.length(); i++){
			if(tkn.str.charAt(i) == ',')
				return false;
		}
		return true;
	}
}
