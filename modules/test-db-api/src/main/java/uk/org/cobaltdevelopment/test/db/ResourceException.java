package uk.org.cobaltdevelopment.test.db;

@SuppressWarnings("serial")
public class ResourceException extends RuntimeException {

	private static final String MESSAGE = "Resource error while carrying out DB UNit Tesitng operation";

	public ResourceException() {
		super(MESSAGE);
	}

	public ResourceException(Exception e) {
		super(MESSAGE, e);
	}
}
