package com.greenmoonsoftware.tea

class Result {
	enum Condition { SUCCESS, FAIL, WARN }
	
	Condition condition
	String message
	
}
