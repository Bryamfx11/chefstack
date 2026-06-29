package com.chefstack.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

// valida el ID token de Firebase con las llaves publicas de Google (sin service account)
@Component
public class FirebaseTokenVerifier {

    private static final Logger log = LoggerFactory.getLogger(FirebaseTokenVerifier.class);
    private static final String CERTS_URL =
            "https://www.googleapis.com/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com";

    private final String projectId;
    private final RestTemplate restTemplate;

    // cache de llaves publicas
    private volatile Map<String, RSAPublicKey> cacheLlaves = new HashMap<>();
    private volatile Instant expiraCache = Instant.EPOCH;

    public FirebaseTokenVerifier(@Value("${firebase.project-id}") String projectId, RestTemplate restTemplate) {
        this.projectId = projectId;
        this.restTemplate = restTemplate;
    }

    // devuelve el uid si el token es valido
    public String verificarYObtenerUid(String token) {
        DecodedJWT jwt = JWT.decode(token);
        RSAPublicKey llave = obtenerLlave(jwt.getKeyId());

        Algorithm algoritmo = Algorithm.RSA256(llave, null);
        String emisor = "https://securetoken.google.com/" + projectId;

        JWT.require(algoritmo)
                .withIssuer(emisor)
                .withAudience(projectId)
                .build()
                .verify(token);

        return jwt.getSubject();
    }

    // llave publica segun el kid del token
    private RSAPublicKey obtenerLlave(String kid) {
        if (Instant.now().isAfter(expiraCache) || !cacheLlaves.containsKey(kid)) {
            refrescarLlaves();
        }
        RSAPublicKey llave = cacheLlaves.get(kid);
        if (llave == null) {
            throw new IllegalStateException("No se encontro la llave publica de Firebase para kid=" + kid);
        }
        return llave;
    }

    @SuppressWarnings("unchecked")
    private synchronized void refrescarLlaves() {
        Map<String, String> certificados = restTemplate.getForObject(CERTS_URL, Map.class);
        if (certificados == null) {
            throw new IllegalStateException("No se pudieron descargar las llaves publicas de Firebase");
        }
        Map<String, RSAPublicKey> nuevas = new HashMap<>();
        certificados.forEach((kid, pem) -> nuevas.put(kid, convertirPemAClave(pem)));

        this.cacheLlaves = nuevas;
        this.expiraCache = Instant.now().plus(Duration.ofHours(1));
        log.debug("Llaves publicas de Firebase actualizadas ({} certificados)", nuevas.size());
    }

    // PEM X.509 -> llave publica RSA
    private RSAPublicKey convertirPemAClave(String pem) {
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) factory.generateCertificate(
                    new ByteArrayInputStream(pem.getBytes(StandardCharsets.UTF_8)));
            return (RSAPublicKey) cert.getPublicKey();
        } catch (Exception ex) {
            throw new IllegalStateException("Certificado de Firebase invalido", ex);
        }
    }
}
