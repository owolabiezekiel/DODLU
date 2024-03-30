package com.dodlu.core.domain.user.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum TokenType {
    ResetPassword;

    @JsonCreator
    public static TokenType create(String value){
        if(isNullOrBlank(value)){
            return null;
        }

        return Stream.of(TokenType.values())
                .filter(t -> t.toString().equalsIgnoreCase(value))
                .findFirst()
                .orElse(ResetPassword);
    }
}
