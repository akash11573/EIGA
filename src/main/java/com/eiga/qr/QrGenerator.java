package com.eiga.qr;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class QrGenerator {
    public static String generatePayload(String bookingId, long issuedAtMillis, long expiresAtMillis) throws Exception {
        String ticketId = bookingId;
        String dataToSign = ticketId + "," + bookingId + "," + issuedAtMillis + "," + expiresAtMillis;
        String hmac = computeHmac(dataToSign);
        String tokenData = issuedAtMillis + ":" + expiresAtMillis + ":" + hmac;
        String signedToken = Base64.getEncoder().encodeToString(tokenData.getBytes(StandardCharsets.UTF_8));
        return "EIGA|" + ticketId + "|" + signedToken;
    }
    
    public static String computeHmac(String data) throws Exception {
        String secret = System.getenv("EIGA_HMAC_SECRET");
        if (secret == null || secret.isEmpty()) {
            throw new Exception("Missing EIGA_HMAC_SECRET environment variable");
        }
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
}
