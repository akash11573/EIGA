package com.eiga.qr;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class QrValidator {
    public static class QrValidationResult {
        public String status;
        public String bookingId;
        public QrValidationResult(String status) { this.status = status; }
    }

    public static QrValidationResult validateFormatAndSignature(String payload) {
        if (payload == null || !payload.startsWith("EIGA|")) return new QrValidationResult("INVALID_QR");
        String[] parts = payload.split("\\|");
        if (parts.length != 3) return new QrValidationResult("INVALID_QR");
        
        String ticketId = parts[1];
        String signedTokenB64 = parts[2];
        
        String tokenData;
        try {
            tokenData = new String(Base64.getDecoder().decode(signedTokenB64), StandardCharsets.UTF_8);
        } catch(Exception e) { return new QrValidationResult("INVALID_QR"); }
        
        String[] tokenParts = tokenData.split(":");
        if (tokenParts.length != 3) return new QrValidationResult("INVALID_QR");
        
        long issuedAtMillis, expiresAtMillis;
        try {
            issuedAtMillis = Long.parseLong(tokenParts[0]);
            expiresAtMillis = Long.parseLong(tokenParts[1]);
        } catch(Exception e) { return new QrValidationResult("INVALID_QR"); }
        
        String providedHmac = tokenParts[2];
        String expectedData = ticketId + "," + ticketId + "," + issuedAtMillis + "," + expiresAtMillis;
        
        try {
            String expectedHmac = QrGenerator.computeHmac(expectedData);
            if (!expectedHmac.equals(providedHmac)) return new QrValidationResult("INVALID_TOKEN");
        } catch(Exception e) { return new QrValidationResult("INVALID_TOKEN"); }
        
        if (System.currentTimeMillis() > expiresAtMillis) return new QrValidationResult("TOKEN_EXPIRED");
        
        QrValidationResult res = new QrValidationResult("VALID");
        res.bookingId = ticketId;
        return res;
    }
}
