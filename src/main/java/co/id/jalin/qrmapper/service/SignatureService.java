package co.id.jalin.qrmapper.service;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
public class SignatureService {

    public String generateSignatureDigestInstance(String value, String digestKey) throws NoSuchAlgorithmException {
        var digest = MessageDigest.getInstance(digestKey);
        var hashBytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hashBytes);
    }

    public String generateSignatureMacInstance(String value, String secretKey, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException {
        var mac = Mac.getInstance(algorithm);
        var secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),algorithm);
        mac.init(secretKeySpec);
        var signatureBytes = mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(signatureBytes);
    }
}
