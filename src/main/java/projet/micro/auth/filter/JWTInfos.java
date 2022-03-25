package projet.micro.auth.filter;

public class JWTInfos
{
	public static final String AUTH_HEADER="AUTHORIZATION";
    public static final String PREFIX="Bearer ";
    public static final long EXPIRE_ACCES_TOKEN=10*60*1000;
    public static final long EXPIRE_REFRESH_TOKEN=20*60*1000;
}
