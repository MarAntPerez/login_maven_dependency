package exceptions;

public class TokenInvalidException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String MSG = "El token ingresado es invalido, que podria estar siendo manipulado";

	public TokenInvalidException() {
		super(MSG);
	}
}
