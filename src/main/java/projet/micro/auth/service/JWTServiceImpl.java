package projet.micro.auth.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.RequiredArgsConstructor;
import projet.micro.auth.model.User;
import projet.micro.auth.repo.UserRepository;
import projet.micro.auth.utils.JWTUtils;


@Service
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService
{
	private final UserRepository userRepository;

	@Override
	public String refreshToken(Algorithm algorithm, String url, String username) {
		System.out.print("I enterred the refreshToken method");
		return JWT.create().withSubject(username)
				.withExpiresAt(new Date(System.currentTimeMillis() + JWTUtils.EXPIRE_REFRESH_TOKEN))
				.withIssuer(url).sign(algorithm);
	}

	@Override
	public String accessToken(List<String> roles, Algorithm algorithm, String url, String username) {
		System.out.print("I enterred the accessToken method");
		User user = userRepository.findByUsername(username);
		return JWT.create().withSubject(username)
				.withExpiresAt(new Date(System.currentTimeMillis() + JWTUtils.EXPIRE_ACCES_TOKEN))
				.withIssuer(url).withArrayClaim("roles", roles.toArray(new String[roles.size()]))
				.withClaim("id", user.getId()).withClaim("username", user.getUsername()).sign(algorithm);
	}

	@Override
	public Map<String, String> jwtTokens(List<String> roles, Algorithm algorithm, String url, String username) {
		System.out.print("I enterred the jwtTokens method");
		Map<String, String> tokens = new HashMap<>();
		tokens.put("accessToken", accessToken(roles, algorithm, url, username));
		tokens.put("refreshToken", refreshToken(algorithm, url, username));
		return tokens;
	}
}
