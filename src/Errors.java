
public enum Errors {
	ERR_SEP(", "),
	ERR_1("Found neither numeric, nor operator element: "),
	ERR_2("There is no operator between values: "),
	ERR_3("There is no value between operators: "),
	ERR_4("Bracket not closed"),
	ERR_5("Too many closing brackets"),
	ERR_6("Found no numeric value in operation"),
	ERR_7("Cannot divide by zero");
	
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
