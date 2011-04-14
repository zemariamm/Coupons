package com.zemariamm.coupons;

public class Transporter {
	private boolean valid;
	private String message;
	public boolean isValid() {
		return valid;
	}
	public String getMessage() {
		return message;
	}
	public Transporter(boolean valid, String message) {
		super();
		this.valid = valid;
		this.message = message;
	}
	
}
