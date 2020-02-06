
public class TestFactory {
	public static void main(String[] args) {
		// Main method to test calculation

		System.out.println("Evaluation tests:");
		System.out.println("=================");
		String is = " = ";
		String result;
		int countTests = 1;
		for (String[] test : Samples.getSamplesList()) {
			result = new CalculatorEngine(test[0]).elementsProcessor();
			System.out.printf("\n%s) %s %s %s \n- correct? %s\n",
					countTests,
					test[0].toString(),
					is,
					result,
					result.equals(test[1].toString())
					);
			countTests++;
		}
	}

}
