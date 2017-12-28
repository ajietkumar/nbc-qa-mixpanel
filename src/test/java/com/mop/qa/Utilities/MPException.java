package com.mop.qa.Utilities;

public class MPException extends Exception {

	private String message = null;

	public MPException() {
		super();
	}

	public MPException(String message) {
		super(message);
		this.message = message;
	}

	public MPException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		return message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}