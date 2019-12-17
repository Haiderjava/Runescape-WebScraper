package gescraper.services;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import gescraper.entities.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService implements InitializingBean{
	byte[] secretBytes;
	
	@Value("${JWT_SECRET:jwtsecret-reallylongsecretrequiredjwtsecret-reallylongsecretrequired}")
	String secret;
	
	public JWTService() {
		super();
	}
	
	// afterPropertiesSet is the appropriate place to initialize them
	@Override
	public void afterPropertiesSet() throws Exception {
		secretBytes = getSecretBytes();
	}
	
	/**
	 * Returns a byte[] of the JWT_SECRET Environment Variable. Be sure to share this with each developer who is building the project.
	 * @return
	 */
	private byte[] getSecretBytes() {
		return secret.getBytes();
	}
	
	
	/**
	 * Returns a SecretKey object, called by JWT creation/read methods
	 * @return
	 */
	private SecretKey getSecret() {
		return Keys.hmacShaKeyFor(secretBytes);
	}

	/**
	 * Returns a JWT with these claims, Issuer: "GEScraper", Subject: userid, IssuedAt: current time,
	 * Expiration: 1 year from issued date, 
	 * The created JWT will be signed with an Environment Variable named ${JWT_SECRET}.
	 * @param user
	 * @return
	 */
	public String signJWT(Users user) {
		String userid = String.valueOf(user.getUserId());
		String jws = Jwts.builder()
						.setIssuer("GEScraper")
						.setSubject(userid)
						.setIssuedAt(new Date())
						// Expires one year from issue date
						.setExpiration(new Date(System.currentTimeMillis() + 365 * 24 * 3600 * 1000))
						.signWith(getSecret())
						.compact();
		return jws;
	}
	

	/**
	 * Attempts to read the UserId from a JWT String and compares it against the given userid
	 * @param jwt
	 * @return
	 */
	public boolean validateJWT(String jwsString, int useridin) {
		try {
			Jws<Claims> jwsclaims = Jwts.parser()        
					.setSigningKey(getSecret())         
					.parseClaimsJws(jwsString);
			int userid = jwsclaims.getBody().get("sub", Integer.class);
			//TODO Should be logging, not System outs
			System.out.println("Attempting JWT auth with UserId: " + useridin + " and JWT UserId: " + userid);
			if(useridin == userid) return true;
			return false;
		} catch (JwtException ex) {
			// TODO Should be logging, not System outs
			System.out.println("JWT Authentication failure...");
			return false;
		}
	}
	
	/**
	 * Attempts to parse claims from a JWS, returns true if it is a valid JWT.
	 * DOES NOT CHECK AGAINST USER IDENTITY.
	 * @param jwsString
	 * @return
	 */
	public boolean validateJWT(String jwsString) {
		try {
			Jws<Claims> jwsclaims = Jwts.parser()        
					.setSigningKey(getSecret())         
					.parseClaimsJws(jwsString);
			return true;
		} catch (JwtException ex) { 
			// TODO Should be logging, not System outs
			System.out.println("JWT Authentication failure...");
			return false;
		}
	}
}