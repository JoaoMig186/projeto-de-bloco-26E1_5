package com.infnet.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class JwtService {
    private final PublicKey publicKey;

    public JwtService(
            @Value("${security.public-key}") Resource rsaPublicKey
    ) {
        try {
            String publicKeyContent =
                    new String(rsaPublicKey.getInputStream().readAllBytes());

            this.publicKey = getPublicKey(publicKeyContent);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar chaves RSA", e);
        }
    }

    public void validateToken(String token) {
       Jwts.parser()
            .verifyWith(publicKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }


    private PublicKey getPublicKey (String rsaPublicKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String key = rsaPublicKey.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }


}
