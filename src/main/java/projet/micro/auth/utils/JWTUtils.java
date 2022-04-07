package projet.micro.auth.utils;

public class JWTUtils 
{
	 private JWTUtils() 
	 {
		 throw new IllegalStateException("Utility class");
	 }
	 
    public static final String SECRET="secret";
   
	public static final String AUTH_HEADER = "AUTHORIZATION";
	public static final String PREFIX = "Bearer ";
	public static final long EXPIRE_ACCES_TOKEN = 1*60*1_000L;
	public static final long EXPIRE_REFRESH_TOKEN = 7*60*1_000L;
	
	public static final String[] PUBLIC_URLS= {"/api/auth/login/**","/api/auth/refreshToken/**"};
}
