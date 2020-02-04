import java.util.ArrayList;
import java.util.List;

public class Samples {
	
private static List<String[]> tests = new ArrayList<>();

	public static List<String[]> samplesList() {
		
		tests.add(new String[] { "127", "127" }); // 127
		tests.add(new String[] { "-127", "-127" }); // -127
		tests.add(new String[] { "2 + 3", "5" }); // 5
		tests.add(new String[] { "2 + 3 +  4", "9" }); // 9
		tests.add(new String[] { "  7  + 3 -   4", "6" }); // 6
		tests.add(new String[] { "2 + 3 + 4 + 5", "14" }); // 14
		tests.add(new String[] { "2 - 6", "-4" }); // -4
		tests.add(new String[] { "2 - 3 - 4", "-5" }); // -5
		tests.add(new String[] { "10 * 5 / 5", "10" }); // 10
		tests.add(new String[] { "10 - 2 * 3", "4" }); // 4
		tests.add(new String[] { "10 * 2 ^ 3", "80" }); // 80
		tests.add(new String[] { "2 * ( 5 - 2 ) ^ 3", "54" }); // 54
		tests.add(new String[] { "10 * 5 / 0", Errors.ERR_VAL_7.getErrDesc()}); // "Cannot divide by zero"
		tests.add(new String[] { "22 * 10 / ( 5 - ( 2 + 3 ) )", Errors.ERR_VAL_7.getErrDesc() }); // "Cannot divide by zero"
		tests.add(new String[] { "2 / 2 + 3 * 4", "13" }); // 13
		tests.add(new String[] { "5 / ( 2 + 3 ) * ( 5 - 1 )", "4" }); // 4
		tests.add(new String[] { "1 + 20 / ( ( 2 + 3 ) * ( 4 - 2 ) )", "3" }); // 3
		tests.add(new String[] { "20 / ( ( 2 + 3 ) * ( 4 - 2 ) ^ 3 )", "0.5" }); // 0.5
		tests.add(new String[] { "7.7 - 3.3 - 4.4", "0.0" }); // 0.0
		tests.add(new String[] { "8.8 - 3.3 - .4", "5.1" }); // 5.1
		tests.add(new String[] { "100 * ( 2 + 12 )", "1400" }); // 1400
		tests.add(new String[] { "100 * ( 2.1 + 12 ) / 20 + 2", "72.5" }); // 72.5
		tests.add(new String[] { "-10 * ( 2.5 - 1.1 )", "-14.0" }); // -14.0
		tests.add(new String[] { "1.652335819E9", "1.652335819E9" }); // 1.652335819E9
		tests.add(new String[] { "-2.4323387584534e11", "-2.4323387584534e11" }); // -2.4323387584534e11
		tests.add(new String[] { "-4.4323387584534exp9", Errors.ERR_VAL_1.getErrDesc()+"-4.4323387584534exp9" }); // Found neither numeric, nor operator element
		tests.add(new String[] { "12 + 3oo - 5", Errors.ERR_VAL_1.getErrDesc()+"3oo" }); // Found neither numeric, nor operator element
		tests.add(new String[] { "3.5 - 1,3 - .4", Errors.ERR_VAL_1.getErrDesc()+"1,3" }); // Found neither numeric, nor operator element
		tests.add(new String[] { "1 # 2 - .4", Errors.ERR_VAL_1.getErrDesc()+"#" }); // Found neither numeric, nor operator element
		tests.add(new String[] { "1 * * 2 - .4", Errors.ERR_VAL_3.getErrDesc()+"*"+Errors.ERR_SEP.getErrDesc()+"*" }); // There is no value between operators
		tests.add(new String[] { "-10  2.5 - 1.1 )", Errors.ERR_VAL_2.getErrDesc()+"-10"+Errors.ERR_SEP.getErrDesc()+"2.5"}); // There is no operator between values :
		tests.add(new String[] { "-10 *  ( ( 2.5 - 1.1 )", Errors.ERR_VAL_4.getErrDesc() }); // Bracket not closed
		tests.add(new String[] { "-10 * ( 2.5 - 1.1 ) ) )", Errors.ERR_VAL_5.getErrDesc() }); // Closing bracket unnecessary
		tests.add(new String[] { " ", Errors.ERR_VAL_1.getErrDesc() }); // Found neither numeric, nor operator element
		tests.add(new String[] { "", Errors.ERR_VAL_1.getErrDesc() }); // Found neither numeric, nor operator element
		
		return tests;
	}

}
