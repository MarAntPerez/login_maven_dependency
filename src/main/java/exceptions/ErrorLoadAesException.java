package exceptions;



public class ErrorLoadAesException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7372617653215990751L;
	private static final String MSG = "El Algoritmo AES no se encuentra disponible en el ambiente, favor de revisar los detalles.";

	public ErrorLoadAesException() {
		super(MSG);
	}
}
