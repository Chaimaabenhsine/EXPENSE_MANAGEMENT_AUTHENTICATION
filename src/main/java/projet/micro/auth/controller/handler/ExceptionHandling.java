package projet.micro.auth.controller.handler;


import java.io.IOException;
import java.util.Objects;
import javax.persistence.NoResultException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.auth0.jwt.exceptions.TokenExpiredException;

import projet.micro.auth.exception.HttpResponse;
import projet.micro.auth.exception.UserNotFoundException;
import projet.micro.auth.exception.UsernameExistException;


@RestControllerAdvice
public class ExceptionHandling 
{
	private static final String NOT_ENOUGH_PERMISSION = "You don not have enough permission";
	private static final String METHOD_NOT_ALLOWED="The method is not allowed";
	private static final String INTERNAL_SERVER_ERROR="Server internal error";
	private static final String PAGE_NOT_FOUND="This page was not found";

	
	@ExceptionHandler(UsernameExistException.class)
	public ResponseEntity<HttpResponse> unsernameAlreadyExistException(UsernameExistException exception)
	{
		return createHttpResponse(HttpStatus.BAD_REQUEST,exception.getMessage());
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<HttpResponse> badCredentialsException(BadCredentialsException exception)
	{
		return createHttpResponse(HttpStatus.BAD_REQUEST,exception.getMessage());
	}

	
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<HttpResponse> noHandlerFoundxception(NoHandlerFoundException e)
	{
		return createHttpResponse(HttpStatus.BAD_REQUEST,PAGE_NOT_FOUND);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<HttpResponse> accessDeniedException()
	{
		return createHttpResponse(HttpStatus.FORBIDDEN,NOT_ENOUGH_PERMISSION);
	}
	
	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException exception)
	{
		return createHttpResponse(HttpStatus.UNAUTHORIZED,exception.getMessage().toUpperCase());
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException exception)
	{
		return createHttpResponse(HttpStatus.BAD_REQUEST,exception.getMessage().toUpperCase());
	}
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<HttpResponse> internalServerErrorException()
	{
		return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR,INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception)
	{
		HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods().iterator().next());
		return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED,String.format(METHOD_NOT_ALLOWED, supportedMethod));
	}
	
	@ExceptionHandler(NoResultException.class)
	public ResponseEntity<HttpResponse> notFoundException(NoResultException exception)
	{
		return createHttpResponse(HttpStatus.NOT_FOUND,exception.getMessage());
	}
	
	private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus,String message)
	{
		return new ResponseEntity<>(new HttpResponse(httpStatus.value(),httpStatus,httpStatus.getReasonPhrase().toUpperCase(),message.toUpperCase()),httpStatus);
	}

}
