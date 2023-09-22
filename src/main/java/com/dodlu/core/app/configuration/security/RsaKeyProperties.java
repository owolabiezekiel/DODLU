package com.dodlu.core.app.configuration.security;

import static com.dodlu.core.app.configuration.security.KeyPairGeneratorUtility.generateRsaKey;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.springframework.stereotype.Component;

@Component
public class RsaKeyProperties {
  private final RSAPublicKey publicKey;
  private final RSAPrivateKey privateKey;

  public RsaKeyProperties() {
    KeyPair keyPair = generateRsaKey();
    this.publicKey = (RSAPublicKey) keyPair.getPublic();
    this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
  }

  public RSAPublicKey getPublicKey() {
    return this.publicKey;
  }

  public RSAPrivateKey getPrivateKey() {
    return this.privateKey;
  }
}
