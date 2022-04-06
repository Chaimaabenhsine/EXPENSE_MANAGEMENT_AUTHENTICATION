package projet.micro.auth.exception;

import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class HttpResponse 
{
	private int httpStatusCode;
	private HttpStatus httpStatus;
	private String reason;
	private String message;
}
