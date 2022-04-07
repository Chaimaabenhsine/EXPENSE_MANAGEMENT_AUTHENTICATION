package projet.micro.auth.exception;

public class BadCredentialsException extends Exception
{
	
	private static final long serialVersionUID = 1L;

	public BadCredentialsException (String message)
	{
		super(message);
	}

}
