package com.intesoft.syncworks.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.intesoft.syncworks.domain.entities.User;
import com.intesoft.syncworks.domain.repositories.UserRepository;
import com.intesoft.syncworks.exceptions.AppException;
import com.intesoft.syncworks.interfaces.dto.UserDto;
import com.intesoft.syncworks.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;


@RequiredArgsConstructor
@Component
public class UserAuthProvider {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Value("${security.jwt.token.secret-kay:secret-key}")
    private String secretKey;

    @PostMapping
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken( UserDto userDto) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3_600_000);
        return JWT.create()
                .withIssuer(userDto.getUserName())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("name", userDto.getName())
                .withClaim("lastName", userDto.getLastName())
                .sign(Algorithm.HMAC256(secretKey));
    }

    public Authentication validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier jwtVerifier = JWT.require(algorithm).build();

        DecodedJWT decodedJWT = jwtVerifier.verify(token);

        UserDto user = UserDto.builder()
                .userName(decodedJWT.getIssuer())
                .name(decodedJWT.getClaim("name").asString())
                .lastName(decodedJWT.getClaim("lastName").asString())
                .build();


        return new UsernamePasswordAuthenticationToken(user,null, Collections.emptyList());
    }

    public Authentication validateTokenStrongly(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier jwtVerifier = JWT.require(algorithm).build();

        DecodedJWT decodedJWT = jwtVerifier.verify(token);

       User user =  userRepository.findByUserName(decodedJWT.getIssuer())
                .orElseThrow(() ->  new AppException("Unknown user", HttpStatus.NOT_FOUND));
       return new UsernamePasswordAuthenticationToken(userMapper.toUserDto(user),null, Collections.emptyList());
    }
}
