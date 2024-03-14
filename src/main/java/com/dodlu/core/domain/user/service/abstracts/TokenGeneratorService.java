package com.dodlu.core.domain.user.service.abstracts;

import java.util.function.Consumer;

public interface TokenGeneratorService {
    String generateToken(String username, Consumer<String> deleteTokenIfExists);
}
