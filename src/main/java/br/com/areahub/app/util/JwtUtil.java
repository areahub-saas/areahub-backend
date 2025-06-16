package br.com.areahub.app.util;

import br.com.areahub.app.exception.BusinessException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final String SYSTEM_NAME = "AUTH_AREA_HUB";
    private static final Long CURRENT_TIME = System.currentTimeMillis() + 600000;

    public String createToken(Authentication authentication, UUID organizationId){
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

            String username = authentication.getPrincipal().toString();
            String authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

            var jwtBuilder = JWT.create()
                    .withIssuer(SYSTEM_NAME)
                    .withSubject(username)
                    .withClaim("authorities", authorities)
                    .withJWTId(UUID.randomUUID().toString())
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(CURRENT_TIME));

            if (organizationId != null) {
                jwtBuilder.withClaim("organization_id", organizationId.toString());
            }

            log.info("Creating JWT token for user: {}", username);
            return jwtBuilder.sign(algorithm);

        } catch (JWTCreationException exception){
            log.error("Error creating JWT token: {}", exception.getMessage());
            throw new BusinessException("Unable to generate token");
        }
    }

    public DecodedJWT validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(SYSTEM_NAME)
                    .build();

            log.info("Validating JWT token: {}", token);
            return verifier.verify(token);
        } catch (JWTVerificationException exception){
            log.error("JWT token validation failed: {}", exception.getMessage());
            throw new BusinessException("Token invalid, not Authorized");
        }
    }

    public String extractUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject();
    }

    public Claim getSpecificationClaim(DecodedJWT decodedJWT, String claimName){
        return decodedJWT.getClaim(claimName);
    }

    public Map<String, Claim> getAllClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }

}
