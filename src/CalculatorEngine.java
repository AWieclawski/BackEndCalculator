import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CalculatorEngine {

	public static Double elementsProcess(String expression) {

		String[] elements = expression.split(" ");
		Stack<Double> values = new Stack<>();
		Stack<Character> operators = new Stack<Character>();

		// TODO recognition and distribution of the elements for
		// two stacks: operators and number values

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
		if (elements[i].equals("+") || elements[i].equals("-") || elements[i].equals("*") || elements[i].equals("/"))
			return true;
		else
			return false;
	}

	private static boolean checkSeparator(String[] elements, int i) {
		if (elements[i].equals(" "))
			return true;
		else
			return false;
	}

	public static boolean higherPriorityOfOp(char previousOp, char followingOp)
	// If 'followingOp' has higher or the same operation priority as 'previousOp',
	// returns true. Otherwise returns false.

	{
		if (followingOp == '(' || followingOp == ')')
			return false;
		if ((previousOp == '*' || previousOp == '/') && (followingOp == '+' || followingOp == '-'))
			return false;
		else
			return true;
	}
	
    public static Double workingOnStacks(char op, Double b, Double a) 
    { 
        // Apply an operator 'op' on operands 'a' and 'b'. 
    	// Returns the result. 
        switch (op) 
        { 
        case '+': 
            return a + b; 
        case '-': 
            return a - b; 
        case '*': 
            return a * b; 
        case '/': 
            if (b == 0) 
                throw new
                UnsupportedOperationException("Cannot divide by zero"); 
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
		tests.add("-2.43233875845E9");

		System.out.println("evaluate:");
		for (String test : tests) {
			System.out.println(test.toString() + is + CalculatorEngine.elementsProcess(test) + "\n");
		}
	}

}
