package com.infnet.service;

import com.infnet.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtService(
            @Value("${security.private-key}") Resource rsaPrivateKey,
            @Value("${security.public-key}") Resource rsaPublicKey
    ) {
        try {
            String privateKeyContent =
                    new String(rsaPrivateKey.getInputStream().readAllBytes());

            String publicKeyContent =
                    new String(rsaPublicKey.getInputStream().readAllBytes());

            this.privateKey = getPrivateKey(privateKeyContent);
            this.publicKey = getPublicKey(publicKeyContent);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar chaves RSA", e);
        }
    }

    public String generateToken(User user)  {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("id", user.getId())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(privateKey)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private PrivateKey getPrivateKey(String rsaPrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String key = rsaPrivateKey.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
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

