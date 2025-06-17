package exceptions;



public class AesFailedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7372617653215990751L;
	private static final String MSG = "Ocurrio un error durante la ejecion de cifrado/descifrado de los datos.";

	public AesFailedException() {
		super(MSG);
	}
}
