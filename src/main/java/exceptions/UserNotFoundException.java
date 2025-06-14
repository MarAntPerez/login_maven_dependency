package exceptions;

public class UserNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;
	private static final String MSG = "Usuario no encontrado";
	
	public UserNotFoundException() {
		super(MSG);
	}

}
