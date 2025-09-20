package com.prem.ecommerce.JWTSecurity;

import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;


@Component
public class JwtUtils {

     private static final  Logger logger = LoggerFactory.getLogger("JwtUtils.class");

     @Value("${spring.app.jwtSecret}")
     private String jwtSecretToken;

     @Value("${spring.app.jwtExpiryInMs}")
    private long jwtExpiryInMs ;

    public String generateJwtFromUsername(UserDetails userDetails)
    {
        String userName = userDetails.getUsername();

        return Jwts.builder()
        .subject(userName)
        .issuedAt(new Date())
        .expiration(new Date(new Date().getTime()+jwtExpiryInMs))
        .signWith(key())
        .compact();
    }





    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header: {} ",bearerToken);

        if(bearerToken!=null && bearerToken.startsWith("Bearer "))
        {
            return bearerToken.substring(7);
        }
        
        return null;
        
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parser()
            .verifyWith(key())
            .build()
            .parseSignedClaims(token);
            return true;
        }
        catch(MalformedJwtException e)
      {
         logger.error("Invalid JWT token: {}", e.getMessage());
      }
      catch(ExpiredJwtException e)
      {
         logger.error("JWT token is expired: {}", e.getMessage());
      }
      catch(UnsupportedJwtException e)
      {
         logger.error("JWT token is unsupported: {}", e.getMessage());
      }
      catch(IllegalArgumentException e)
      {
         logger.error("JWT claims string is empty: {}", e.getMessage());
      }

      return false;
       
    }

    public String getUserNameFromJwt(String jwt)
    {
        return Jwts.parser()
        .verifyWith(key())
        .build()
        .parseSignedClaims(jwt)
        .getPayload()
        .getSubject();
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretToken));
    }

}
