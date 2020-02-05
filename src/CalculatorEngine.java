import java.math.BigDecimal;
import java.util.Stack;

public class CalculatorEngine {

	final static String expressionSeparator = "\\s";

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

	String elementsProcessor() {

		String expression = CalculatorEngine.getX();

		String checkedExpression;

		if (expression.length() > 0)
			checkedExpression = replaceMultiSpaces(expression);
		else
			checkedExpression = expression;

		String[] elements = checkedExpression.split(expressionSeparator);
		Stack<String> values = new Stack<>();
		Stack<String> operators = new Stack<>();
		Stack<String> bracketOpen = new Stack<>();

		for (int i = 0; i < elements.length; i++) {

			if (checkBigDecimal(elements[i]))
				values.push(elements[i]);

			// If neither numeric nor operator recognized, returns error communicate
			if (!checkBigDecimal(elements[i]) && !checkOperatorOrBracket(elements[i]))
				return Errors.ERR_VAL_1.getErrDesc().concat(elements[i]);

			// error, if between values there is no operator
			if (!checkIfLastElement(elements, i) && checkBigDecimal(elements[i]) && checkBigDecimal(elements[i + 1]))
				return Errors.ERR_VAL_2.getErrDesc().concat(elements[i]).concat(Errors.ERR_SEP.getErrDesc()).concat(elements[i + 1]);

			// error, if between operators there is no value
			if (!checkIfLastElement(elements, i) && checkOperator(elements[i]) && checkOperator(elements[i + 1]))
				return Errors.ERR_VAL_3.getErrDesc().concat(elements[i]).concat(Errors.ERR_SEP.getErrDesc()).concat(elements[i + 1]);

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
					return Errors.ERR_VAL_5.getErrDesc();
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
			return Errors.ERR_VAL_4.getErrDesc(); // if bracket not closed
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
			return new BigDecimal(testedElement.replaceAll(Errors.ERR_VAL_8.getErrDesc(), null));
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
		if (testedElement.equals(expressionSeparator))
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

	private String workingOnStacks(String op, String b, String a)
	// Apply an operator 'op' on operands 'a' and 'b'.
	// Returns the result.
	{
		BigDecimal aBD, bBD;
		if (checkBigDecimal(a)) {
			aBD = stringToBD(a);
		} else
			return Errors.ERR_VAL_6.getErrDesc();
		if (checkBigDecimal(b)) {
			bBD = stringToBD(b);
		} else
			return Errors.ERR_VAL_6.getErrDesc();

		switch (op) {
		case "+":
			return aBD.add(bBD).toPlainString();
		case "-":
			return aBD.subtract(bBD).toPlainString();
		case "*":
			return aBD.multiply(bBD).toPlainString();
		case "/":
			if (bBD.equals(BigDecimal.ZERO))
				return Errors.ERR_VAL_7.getErrDesc();
			else
				return aBD.divide(bBD).toPlainString();
		case "^":
			return aBD.pow(bBD.intValue()).toPlainString();
		}
		return BigDecimal.ZERO.toPlainString();
	}

}
