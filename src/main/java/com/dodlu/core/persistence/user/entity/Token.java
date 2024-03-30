package com.dodlu.core.persistence.user.entity;

import com.dodlu.core.domain.user.enums.TokenType;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Objects;


@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tokens")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Token {
    @Id @UuidGenerator() protected String id;
    private String username;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    private String token;
    private Instant timeToLive;

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        if(!super.equals(o)) return false;
        Token that = (Token) o;
        return id.equals(that.id);
    }
}
