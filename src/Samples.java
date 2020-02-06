import java.util.ArrayList;
import java.util.List;

public class Samples {
	
private static List<String[]> tests = new ArrayList<>();

private static String[][]sampArr = new String[][] {
		{ "127", "127" }, // 127
		{ "-127", "-127" }, // -127
		{ "2 + 3", "5" }, // 5
		{ "2 + 3 +  4", "9" }, // 9
		{ "  7  + 3 -   4", "6" }, // 6
		{ "2 + 3 + 4 + 5", "14" }, // 14
		{ "2 - 6", "-4" }, // -4
		{ "2 - 3 - 4", "-5" }, // -5
		{ "10 * 5 / 5", "10" }, // 10
		{ "10 - 2 * 3", "4" }, // 4
		{ "10 * 2 ^ 3", "80" }, // 80
		{ "2 * ( 5 - 2 ) ^ 3", "54" }, // 54
		{ "10 * 5 / 0", Errors.ERR_7.getErrDesc()}, // "Cannot divide by zero"
		{ "22 * 10 / ( 5 - ( 2 + 3 ) )", Errors.ERR_7.getErrDesc() }, // "Cannot divide by zero"
		{ "2 / 2 + 3 * 4", "13" }, // 13
		{ "5 / ( 2 + 3 ) * ( 5 - 1 )", "4" }, // 4
		{ "1 + 20 / ( ( 2 + 3 ) * ( 4 - 2 ) )", "3" }, // 3
		{ "20 / ( ( 2 + 3 ) * ( 4 - 2 ) ^ 3 )", "0.5" }, // 0.5
		{ "7.7 - 3.3 - 4.4", "0.0" }, // 0.0
		{ "8.8 - 3.3 - .4", "5.1" }, // 5.1
		{ "100 * ( 2 + 12 )", "1400" }, // 1400
		{ "100 * ( 2.1 + 12 ) / 20 + 2", "72.5" }, // 72.5
		{ "-10 * ( 2.5 - 1.1 )", "-14.0" }, // -14.0
		{ "1.652335819E9", "1.652335819E9" }, // 1.652335819E9
		{ "-2.4323387584534e11", "-2.4323387584534e11" }, // -2.4323387584534e11
		{ "-4.4323387584534exp9", Errors.ERR_1.getErrDesc()+"-4.4323387584534exp9" }, // Found neither numeric, nor operator element
		{ "12 + 3oo - 5", Errors.ERR_1.getErrDesc()+"3oo" }, // Found neither numeric, nor operator element
		{ "3.5 - 1,3 - .4", Errors.ERR_1.getErrDesc()+"1,3" }, // Found neither numeric, nor operator element
		{ "1 # 2 - .4", Errors.ERR_1.getErrDesc()+"#" }, // Found neither numeric, nor operator element
		{ "1 * * 2 - .4", Errors.ERR_3.getErrDesc()+"*"+Errors.ERR_SEP.getErrDesc()+"*" }, // There is no value between operators
		{ "-10  2.5 - 1.1 )", Errors.ERR_2.getErrDesc()+"-10"+Errors.ERR_SEP.getErrDesc()+"2.5"}, // There is no operator between values :
		{ "-10 *  ( ( 2.5 - 1.1 )", Errors.ERR_4.getErrDesc() }, // Bracket not closed
		{ "-10 * ( 2.5 - 1.1 ) ) )", Errors.ERR_5.getErrDesc() }, // Closing bracket unnecessary
		{ " ", Errors.ERR_1.getErrDesc() }, // Found neither numeric, nor operator element
		{ "", Errors.ERR_1.getErrDesc() } // Found neither numeric, nor operator element
	};

	static List<String[]> getSamplesList() {
		
		for (String[]s:sampArr) tests.add(s);
		
		return tests;
	}

}
