import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CalculatorEngine {

	final static String separator = "\\s";
	final static String valueSeparator = ", ";
	final static String errValue1 = "Found neither numeric, nor operator element";
	final static String errValue2 = "There is no operator between values: ";
	final static String errValue3 = "There is no value between operators: ";
	final static String errValue4 = "Bracket not closed";
	final static String errValue5 = "Closing bracket unnecessary";
	final static String errValue6 = "Found no numeric value in operation";
	final static String errDivZero = "Cannot divide by zero";
	final static String nonNumerics = "[^\\d.]";

	public CalculatorEngine(String expression) {
		setX(expression);
	}

	private static String x;

	public static String getX() {
		return x;
	}

	public void setX(String x) {
		CalculatorEngine.x = x;
	}

	public String elementsProcessor() {

		String expression = CalculatorEngine.getX();

		String checkedExpression;

		if (expression.length() > 0)
			checkedExpression = replaceMultiSpaces(expression);
		else
			checkedExpression = expression;

		String[] elements = checkedExpression.split(separator);
		Stack<String> values = new Stack<>();
		Stack<String> operators = new Stack<>();
		Stack<String> bracketOpen = new Stack<>();

		for (int i = 0; i < elements.length; i++) {

			if (checkBigDecimal(elements[i]))
				values.push(elements[i]);

			// If neither numeric nor operator recognized, returns error communicate
			if (!checkBigDecimal(elements[i]) && !checkOperatorOrBracket(elements[i]))
				return errValue1.concat(valueSeparator).concat(elements[i]);

			// error, if between values there is no operator
			if (!checkIfLastElement(elements, i) && checkBigDecimal(elements[i]) && checkBigDecimal(elements[i + 1]))
				return errValue2.concat(elements[i]).concat(valueSeparator).concat(elements[i + 1]);

			// error, if between operators there is no value
			if (!checkIfLastElement(elements, i) && checkOperator(elements[i]) && checkOperator(elements[i + 1]))
				return errValue3.concat(elements[i]).concat(valueSeparator).concat(elements[i + 1]);

			else if (checkSeparator(elements[i]))
				continue;

			else if (elements[i].equals("(")) {
				bracketOpen.push(elements[i]); // brackets begin stacked
				operators.push(elements[i]);
			}

			else if (elements[i].equals(")"))
			// If closing bracket ")" is encountered,
			// takes operators and operands from stacks
			// till open bracket "("
			{
				if (bracketOpen.empty())
					return errValue5;
				while (!operators.peek().equals("("))
					values.push(workingOnStacks(operators.pop(), values.pop(), values.pop()));
				// remove start bracket "(" from 'operators' stack
				bracketOpen.pop();
				operators.pop();

				// finish operation waiting before bracket or brackets, if any
				if (checkIfLastElement(elements, i)) { // check if current is the last operation
					while (!operators.empty() && !checkBracket(operators.peek()))
						values.push(workingOnStacks(operators.pop(), values.pop(), values.pop()));
				} else { // check if following operator has higher priority than current
					while (!operators.empty() && !checkBracket(operators.peek())
							&& higherPriorityOp(elements[i + 1], operators.peek()))
						values.push(workingOnStacks(operators.pop(), values.pop(), values.pop()));
				}
			}

			else if (checkOperator(elements[i])) {
				// While top operator from 'operators' stack has the same or higher priority
				// to current element and operator, as well. Use the operator from 'operators'
				// with top two elements in 'values' stack
				while (!operators.empty() && higherPriorityOp(elements[i], operators.peek()))
					values.push(workingOnStacks(operators.pop(), values.pop(), values.pop()));

				// Push current element to 'operators'.
				operators.push(elements[i]);
			}
		}
		if (!bracketOpen.empty())
			return errValue4; // if bracket not closed
		while (!operators.empty())
			values.push(workingOnStacks(operators.pop(), values.pop(), values.pop()));
		return values.pop();
	}

	private String replaceMultiSpaces(String testedElement) {
		return testedElement.replaceAll("(\\s)+", " ").trim();
	}

	private boolean checkBigDecimal(String testedElement) {
		try {
			new BigDecimal(testedElement);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private BigDecimal stringToBD(String testedElement) {
		try {
			return new BigDecimal(testedElement);
		} catch (NumberFormatException e) {
			return new BigDecimal(testedElement.replaceAll(nonNumerics, null));
		}
	}

	private boolean checkOperator(String testedElement) {
		if (testedElement.equals("+") || testedElement.equals("-") || testedElement.equals("*")
				|| testedElement.equals("/") || testedElement.equals("^"))
			return true;
		else
			return false;
	}

	private boolean checkBracket(String testedElement) {
		if (testedElement.equals("(") || testedElement.equals(")"))
			return true;
		else
			return false;
	}

	private int checkLevelOp(String testedElement) {
		if (testedElement.equals("+") || testedElement.equals("-"))
			return 1;
		if (testedElement.equals("*") || testedElement.equals("/"))
			return 2;
		if (testedElement.equals("^"))
			return 3;
		return 0;
	}

	private boolean checkOperatorOrBracket(String testedElement) {
		if (checkOperator(testedElement) || checkBracket(testedElement))
			return true;
		else
			return false;
	}

	private boolean checkSeparator(String testedElement) {
		if (testedElement.equals(separator))
			return true;
		else
			return false;
	}

	private boolean checkIfLastElement(String[] elements, int i) {
		if (i == elements.length - 1)
			return true;
		else
			return false;
	}

	public boolean higherPriorityOp(String previousOp, String followingOp)
	// If 'followingOp' has higher or the same operation priority as 'previousOp',
	// returns true. Otherwise returns false.
	{
		if (checkBracket(followingOp))
			return false;
		if (checkLevelOp(followingOp) < checkLevelOp(previousOp))
			return false;
		else
			return true;
	}

	public String workingOnStacks(String op, String b, String a)
	// Apply an operator 'op' on operands 'a' and 'b'.
	// Returns the result.
	{
		BigDecimal aBD, bBD;
		if (checkBigDecimal(a)) {
			aBD = stringToBD(a);
		} else
			return errValue6;
		if (checkBigDecimal(b)) {
			bBD = stringToBD(b);
		} else
			return errValue6;

		switch (op) {
		case "+":
			return aBD.add(bBD).toPlainString();
		case "-":
			return aBD.subtract(bBD).toPlainString();
		case "*":
			return aBD.multiply(bBD).toPlainString();
		case "/":
			if (bBD.equals(BigDecimal.ZERO))
				return errDivZero;
			else
				return aBD.divide(bBD).toPlainString();
		case "^":
			return aBD.pow(bBD.intValue()).toPlainString();
		}
		return BigDecimal.ZERO.toPlainString();
	}

	public static void main(String[] args) {
		// Main method to test calculation

		String is = " = ";

		List<String[]> tests = new ArrayList<>();
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
		tests.add(new String[] { "10 * 5 / 0", errDivZero }); // "Cannot divide by zero"
		tests.add(new String[] { "22 * 10 / ( 5 - ( 2 + 3 ) )", errDivZero }); // "Cannot divide by zero"
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
		tests.add(new String[] { "-4.4323387584534exp9", errValue1+valueSeparator+"-4.4323387584534exp9" }); // Found neither numeric, nor operator element
		tests.add(new String[] { "12 + 3oo - 5", errValue1+valueSeparator+"3oo" }); // Found neither numeric, nor operator element
		tests.add(new String[] { "3.5 - 1,3 - .4", errValue1+valueSeparator+"1,3" }); // Found neither numeric, nor operator element
		tests.add(new String[] { "1 # 2 - .4", errValue1+valueSeparator+"#" }); // Found neither numeric, nor operator element
		tests.add(new String[] { "1 * * 2 - .4", errValue3+"*"+valueSeparator+"*" }); // There is no value between operators
		tests.add(new String[] { "-10  2.5 - 1.1 )", errValue2+"-10"+valueSeparator+"2.5" }); // There is no operator between values :
		tests.add(new String[] { "-10 *  ( ( 2.5 - 1.1 )", errValue4 }); // Bracket not closed
		tests.add(new String[] { "-10 * ( 2.5 - 1.1 ) ) )", errValue5 }); // Closing bracket unnecessary
		tests.add(new String[] { " ", errValue1+valueSeparator }); // Found neither numeric, nor operator element
		tests.add(new String[] { "", errValue1+valueSeparator }); // Found neither numeric, nor operator element

		System.out.println("Evaluate:");
		String result;
		for (String[] test : tests) {
			result = new CalculatorEngine(test[0]).elementsProcessor();
			System.out.println(test[0].toString() + is + result + "\t- correct? " + result.equals(test[1].toString()));
		}
	}
}
