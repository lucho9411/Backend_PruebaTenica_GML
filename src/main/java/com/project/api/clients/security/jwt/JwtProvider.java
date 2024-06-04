package com.project.api.clients.security.jwt;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtProvider {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(JwtProvider.class);
	
	@Value("${jwt.secret}")
	private String secret;
	@Value("${token.signing.key}")
    private String jwtSigningKey;
	@Value("${jwt.expiration}")
	private int expiration;
	
	public String generateToken(UserDetails userDetails, long id) {
		LOGGER.info("######### - Generando token - #########");
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("UserID", id)
                .claim("Authority", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
	
	 public Authentication getAuthentication(String token) {
		 LOGGER.info("######### - Autenticando usuario - #########");
        Claims claims = getClaims(token);
        @SuppressWarnings("unchecked")
		Collection<? extends GrantedAuthority> authorities = Stream.of(claims.get("Authority"))
                .map(role -> (List<Map<String, String>>) role)
                .flatMap(role -> role.stream()
                        .map(r -> r.get("authority"))
                        .map(SimpleGrantedAuthority::new))
                .collect(Collectors.toList());
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public Claims getClaims(String token) {
    	LOGGER.info("######### - Obteniendo parámetros del token - #########");
    	return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public String getSubject(String token) {
    	LOGGER.info("######### - Obteniendo Usuario del token - #########");
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
	
	public String getUsernameFromToken(String token) {
		LOGGER.info("######### - Obteniendo Usuario del token - #########");
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
		}
		catch(MalformedJwtException mfe) {
			LOGGER.error("######### - Token malformado - #########");
			return false;
		}
		catch(UnsupportedJwtException use) {
			LOGGER.error("######### - Token no soportado - #########");
			return false;
		}
		catch(ExpiredJwtException exe) {
			LOGGER.error("######### - Token expirado - #########");
			return false;
		}
		catch(IllegalArgumentException iae) {
			LOGGER.error("######### - Token vacío - #########");
			return false;
		}
		catch(SignatureException sge) {
			LOGGER.error("######### - Fallo con la firma del token - #########");
			return false;
		}
	}
	
	public String validateTokenMessage(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return "Ok";
		}
		catch(MalformedJwtException mfe) {
			LOGGER.error("######### - Token malformado - #########");
			return "Token malformado";
		}
		catch(UnsupportedJwtException use) {
			LOGGER.error("######### - Token no soportado - #########");
			return "Token no soportado";
		}
		catch(ExpiredJwtException exe) {
			LOGGER.error("######### - Token expirado - #########");
			return "Token expirado";
		}
		catch(IllegalArgumentException iae) {
			LOGGER.error("######### - Token vacío - #########");
			return "Token vacío";
		}
		catch(SignatureException sge) {
			LOGGER.error("######### - Fallo con la firma del token - #########");
			return "Fallo con la firma del token";
		}
	}

}
