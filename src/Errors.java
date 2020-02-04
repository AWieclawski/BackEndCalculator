
public enum Errors {
	ERR_SEP(", "),
	ERR_VAL_1("Found neither numeric, nor operator element: "),
	ERR_VAL_2("There is no operator between values: "),
	ERR_VAL_3("There is no value between operators: "),
	ERR_VAL_4("Bracket not closed"),
	ERR_VAL_5("Closing bracket unnecessary"),
	ERR_VAL_6("Found no numeric value in operation"),
	ERR_VAL_7("Cannot divide by zero"),
	ERR_VAL_8("[^\\d.]");
	
	private String errDesc;

	Errors(String errDesc){
		this.setErrDesc(errDesc);
	}

	public String getErrDesc() {
		return errDesc;
	}

	public void setErrDesc(String errDesc) {
		this.errDesc = errDesc;
	}
	
}
