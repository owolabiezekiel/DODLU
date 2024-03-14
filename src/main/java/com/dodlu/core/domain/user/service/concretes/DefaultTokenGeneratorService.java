package com.dodlu.core.domain.user.service.concretes;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.domain.user.service.abstracts.TokenGeneratorService;
import com.dodlu.core.persistence.user.entity.Token;
import com.dodlu.core.persistence.user.repository.TokenRepository;
import com.dodlu.core.persistence.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultTokenGeneratorService implements TokenGeneratorService {
    private static final Integer TOKEN_LENGTH = 4;
    private static final Integer TOKEN_BOUND = 9;
    private static final Integer maxRetryCount = 3;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private final CoreExceptionService exceptions;

    @Override
    public synchronized String generateToken(String username, Consumer<String> deleteTokenIfExists) {
        validateUserExists(username);
        deleteTokenIfExists.accept(username);
        return generateToken();
    }

    private String generateToken(){
        int tryCount = 0;
        Optional<String> validToken = Optional.empty();
        while(tryCount < maxRetryCount){
            String generatedToken = generateToken(TOKEN_LENGTH, TOKEN_BOUND);
            if(tokenExists(generatedToken)){
                tryCount++;
                continue;
            }
            validToken = Optional.of(generatedToken);
            break;
        }
        return validToken.orElseThrow(
                () ->
                        exceptions.handleCreateBadRequest(
                                "Maximum token generation retry exceeded, please contact administrator."));
    }

    private boolean tokenExists(String generatedToken) {
        Optional<Token> token = tokenRepository.findByToken(generatedToken);
        return token.isPresent();
    }

    private String generateToken(int tokenLength, int tokenBound){
        Random random = new Random();
        StringBuilder generatedToken = new StringBuilder();
        while(generatedToken.length() < tokenLength){
            generatedToken.append(random.nextInt(tokenBound));
        }
        return generatedToken.toString();
    }

    private void validateUserExists(String username) {
        boolean userExists = userRepository.existsByUsernameEqualsIgnoreCase(username);
        if(!userExists){
            throw exceptions.handleCreateBadRequest(
                    format("User with name: %s does not exist.", username)
            );
        }
    }
}
