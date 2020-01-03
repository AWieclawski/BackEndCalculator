import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CalculatorEngine {

	final static String separator = "\\s";
	final static String errValue = "Wrong value or operator";
	final static String errDivZero = "Cannot divide by zero";
	final static String nonNumerics = "[^\\d.]";

	public static String elementsProcessor(String expression) {

		String checkedExpression;

		if (expression.length() > 0)
			checkedExpression = replaceMultiSpaces(expression);
		else
			checkedExpression = expression;

		String[] elements = checkedExpression.split(separator);
		Stack<String> values = new Stack<>();
		Stack<String> operators = new Stack<>();

		for (int i = 0; i < elements.length; i++) {

			if (checkBigDecimal(elements[i]))
				values.push(elements[i]);

			// strings longer than a single operator and not recognized as numbers
			// are reduced to zero
			if (!checkBigDecimal(elements[i]) && !checkIfOperator(elements[i]))
				return errValue;

			else if (checkSeparator(elements[i]))
				continue;

			else if (elements[i].equals("("))
				operators.push(elements[i]);

			else if (elements[i].equals(")"))
			// If closing bracket ")" is encountered,
			// takes operators and operands from stacks
			// till open bracket "("
			{
				while (!operators.peek().equals("("))
					values.push(workingOnStacks(operators.pop(), values.pop(), values.pop()));
				// remove start bracket "(" from 'operators' stack
				operators.pop();

				// finish operation waiting before bracket or brackets, if any
				if (i == elements.length - 1) {	// check if current is the last operation
					while (!operators.empty() && !checkBracket(operators.peek()))
						values.push(workingOnStacks(operators.pop(), values.pop(), values.pop()));
				} else {	// check if following operator has higher priority than current
					while (!operators.empty() && !checkBracket(operators.peek())
							&& higherPriorityOfOp(elements[i + 1], operators.peek()))
						values.push(workingOnStacks(operators.pop(), values.pop(), values.pop()));
				}
			}

			else if (checkOperator(elements[i])) {
				// While top operator from 'operators' stack has the same or higher priority
				// to current element and operator, as well. Use the operator from 'operators'
				// with top two elements in 'values' stack
				while (!operators.empty() && higherPriorityOfOp(elements[i], operators.peek()))
					values.push(workingOnStacks(operators.pop(), values.pop(), values.pop()));

				// Push current element to 'operators'.
				operators.push(elements[i]);
			}
		}

		while (!operators.empty())
			values.push(workingOnStacks(operators.pop(), values.pop(), values.pop()));
		return values.pop();
	}

	private static String replaceMultiSpaces(String testedElement) {
		return testedElement.replaceAll("(\\s)+", " ").trim();
	}

	private static boolean checkBigDecimal(String testedElement) {
		try {
			new BigDecimal(testedElement);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static BigDecimal stringToBD(String testedElement) {
		try {
			return new BigDecimal(testedElement);
		} catch (NumberFormatException e) {
			return new BigDecimal(testedElement.replaceAll(nonNumerics, null));
		}
	}

	private static boolean checkOperator(String testedElement) {
		if (testedElement.equals("+") || testedElement.equals("-") || testedElement.equals("*")
				|| testedElement.equals("/") || testedElement.equals("^"))
			return true;
		else
			return false;
	}

	private static boolean checkBracket(String testedElement) {
		if (testedElement.equals("(") || testedElement.equals(")"))
			return true;
		else
			return false;
	}

	private static int checkLevelOp(String testedElement) {
		if (testedElement.equals("+") || testedElement.equals("-"))
			return 1;
		if (testedElement.equals("*") || testedElement.equals("/"))
			return 2;
		if (testedElement.equals("^"))
			return 3;
		return 0;
	}

	private static boolean checkIfOperator(String testedElement) {
		if (checkOperator(testedElement) || checkBracket(testedElement))
			return true;
		else
			return false;
	}

	private static boolean checkSeparator(String testedElement) {
		if (testedElement.equals(separator))
			return true;
		else
			return false;
	}

	public static boolean higherPriorityOfOp(String previousOp, String followingOp)
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

	public static String workingOnStacks(String op, String b, String a)
	// Apply an operator 'op' on operands 'a' and 'b'.
	// Returns the result.
	{
		BigDecimal aBD, bBD;
		if (checkBigDecimal(a)) {
			aBD = stringToBD(a);
		} else
			return errValue;
		if (checkBigDecimal(b)) {
			bBD = stringToBD(b);
		} else
			return errValue;

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

		List<String> tests = new ArrayList<>();
		tests.add("127"); // 127
		tests.add("-127"); // -127
		tests.add("2 + 3"); // 5
		tests.add("2 + 3 +  4"); // 9 - double spaces removed
		tests.add("  7  + 3 -   4"); // 6 - initial and more multispaces removed
		tests.add("2 + 3 + 4 + 5"); // 14
		tests.add("2 - 6"); // -4
		tests.add("2 - 3 - 4"); // -5
		tests.add("10 * 5 / 5"); // 10
		tests.add("10 - 2 * 3"); // 4
		tests.add("10 * 2 ^ 3"); // 80
		tests.add("2 * ( 5 - 2 ) ^ 3"); // 54
		tests.add("10 * 5 / 0"); // "Cannot divide by zero"
		tests.add("22 * 10 / ( 5 - ( 2 + 3 ) )"); // "Cannot divide by zero"
		tests.add("2 / 2 + 3 * 4"); // 13
		tests.add("5 / ( 2 + 3 ) * ( 5 - 1 )"); // 4 - more brackets
		tests.add("15 / ( ( 2 + 3 ) * ( 2 - 1 ) )"); // 3 - multilevel brackets
		tests.add("7.7 - 3.3 - 4.4"); // 0
		tests.add("8.8 - 3.3 - .4"); // 5.1
		tests.add("100 * ( 2 + 12 )"); // 1400
		tests.add("100 * ( 2.1 + 12 ) / 20 + 2"); // 72.5
		tests.add("-10 * ( 2.5 - 1.1 )"); // -14
		tests.add("1.652335819E9"); // 1652335819
		tests.add("-2.4323387584534e11"); // -243233875845.34
		tests.add("-4.4323387584534exp9"); // Wrong value
		tests.add("12 + 3oo - 5"); // Wrong value
		tests.add("3.5 - 1,3 - .4"); // Wrong value
		tests.add("1 # 2 - .4"); // Wrong value
		tests.add(" "); // Wrong value
		tests.add(""); // Wrong value

		System.out.println("Evaluate:");
		for (String test : tests) {
			System.out.println(test.toString() + is + CalculatorEngine.elementsProcessor(test));
		}
	}
}
