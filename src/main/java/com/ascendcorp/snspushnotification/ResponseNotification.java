package com.ascendcorp.snspushnotification;

public class ResponseNotification {

	@Override
	public String toString() {
		return "ResponseNotify [firstName=" + firstName + ", lastName=" + lastName + ", message=" + message + "]";
	}

	String message;
	String firstName;
	String lastName;
	
	public ResponseNotification(String firstName, String lastName, String message) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.message = message;
	}

	public ResponseNotification() {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}