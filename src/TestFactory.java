
public class TestFactory {
	public static void main(String[] args) {
		// Main method to test calculation

		String is = " = ";
		System.out.println("Evaluation tests:");
		System.out.println("=================");
		String result;
		for (String[] test : Samples.samplesList()) {
			result = new CalculatorEngine(test[0]).elementsProcessor();
			System.out.println(
					"\n" + test[0].toString() + is + result + "\n- correct? " + result.equals(test[1].toString()));
		}
	}

}
