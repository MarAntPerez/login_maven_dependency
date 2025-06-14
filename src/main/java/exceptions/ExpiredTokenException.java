package exceptions;

public class ExpiredTokenException extends Exception{

	private static final long serialVersionUID = 1L;
	private static final String MSG = "El token ha caducado";

	public ExpiredTokenException() {
		super(MSG);
	}
	
}
