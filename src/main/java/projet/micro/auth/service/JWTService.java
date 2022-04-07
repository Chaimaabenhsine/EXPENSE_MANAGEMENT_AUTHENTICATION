package projet.micro.auth.service;

import java.util.List;
import java.util.Map;

import com.auth0.jwt.algorithms.Algorithm;

public interface JWTService 
{
	String refreshToken(Algorithm algorithm, String url, String username);
	 
	String accessToken(List<String> roles, Algorithm algorithm, String url, String username);
	 
	Map<String, String> jwtTokens(List<String> roles, Algorithm algorithm, String url, String username);
}
