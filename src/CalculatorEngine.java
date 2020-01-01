import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CalculatorEngine {

	final static String separator = " ";
	
	public static Double elementsProcessor(String expression) {

		String[] elements = expression.split(separator);
		Stack<Double> values = new Stack<>();
		Stack<String> operators = new Stack<String>();

		for (int i = 0; i < elements.length; i++) {

			if (checkDouble(elements, i)) values.push(Double.valueOf(elements[i]));
			
			// strings longer than a single operator and not recognized as numbers
			// are reduced to zero
			if (!checkDouble(elements, i) && elements[i].length()>1) values.push(0.0);
			
			else if (checkSeparator(elements, i))
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
			}
			
			else if (checkOperator(elements, i)) {
	            // While top operator from 'operators' stack has same or higher priority 
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

	private static boolean checkDouble(String[] elements, int i) {
		try {
			Double.valueOf(elements[i]);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static boolean checkOperator(String[] elements, int i) {
		if (elements[i].equals("+") 
				|| elements[i].equals("-") 
				|| elements[i].equals("*") 
				|| elements[i].equals("/"))
			return true;
		else
			return false;
	}

	private static boolean checkSeparator(String[] elements, int i) {
		if (elements[i].equals(separator))
			return true;
		else
			return false;
	}

	public static boolean higherPriorityOfOp(String previousOp, String followingOp)
	// If 'followingOp' has higher or the same operation priority as 'previousOp',
	// returns true. Otherwise returns false.
	{
		if (followingOp.equals("(") || followingOp.equals(")"))
			return false;
		if ((previousOp.equals("*") || previousOp.equals("/") 
				&& (followingOp.equals("+") || followingOp.equals("-"))))
			return false;
		else
			return true;
	}

	public static Double workingOnStacks(String op, Double b, Double a)
	// Apply an operator 'op' on operands 'a' and 'b'.
	// Returns the result.
	{
		switch (op) {
		case "+":
			return a + b;
		case "-":
			return a - b;
		case "*":
			return a * b;
		case "/":
			if (b == 0)
				throw new UnsupportedOperationException("Cannot divide by zero");
			return a / b;
		}
		return (double) 0;
	}

	public static void main(String[] args) {
		// Main method to test calculation

		String is = " = ";

		List<String> tests = new ArrayList<>();
		tests.add("127"); // 127
		tests.add("-127"); // -127
		tests.add("2 + 3"); // 5
		tests.add("2 + 3 + 4"); // 9
		tests.add("2 + 3 + 4 + 5"); // 14
		tests.add("2 - 6"); // -4
		tests.add("2 - 3 - 4"); // -5
		tests.add("10 * 5 / 5"); // 10
		tests.add("2 / 2 + 3 * 4"); // 13
		tests.add("7.7 - 3.3 - 4.4"); // 0
		tests.add("8.8 - 3.3 - 4.4"); // 1.1
		tests.add("100 * ( 2 + 12 )"); // 1400
		tests.add("100 * ( 2.1 + 12 ) / 20 + 2"); // 72.5
		tests.add("-10 * ( 2.5 - 1.1 )"); // -14
		tests.add("1.652335819E9");
		tests.add("-2.43233875845e9");
		tests.add("-4.4323387584534exp9");
		tests.add("12 + 3oo - 5"); // 7

		System.out.println("evaluate:");
		for (String test : tests) {
			System.out.println(test.toString() + is + CalculatorEngine.elementsProcessor(test).toString() + "\n");
		}
	}

}
