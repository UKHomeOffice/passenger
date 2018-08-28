package org.gov.uk.homeoffice.digital.permissions.passenger.security;

import org.apache.commons.codec.binary.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class TOTP {

    private static final String NUM_DIGITS_OUTPUT = "%06d";

    private String sharedSecret;
    private Integer varianceSeconds;
    private Integer timeStepSeconds;

    @Autowired
    public TOTP(@Value("${totp.secret}") final String sharedSecret,
                @Value("${totp.variance.seconds}") final Integer varianceSeconds,
                @Value("${totp.time.step.seconds}") final Integer timeStepSeconds) {
        this.sharedSecret = sharedSecret;
        this.varianceSeconds = varianceSeconds;
        this.timeStepSeconds = timeStepSeconds;
    }

    public boolean isCodeValid(final String code) throws RuntimeException {
        boolean result;
        try {
            result = verifyCode(code);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("invalid algorithm");
        } catch (InvalidKeyException e) {
            throw new RuntimeException("invalid key");
        }

        return result;
    }

    public String generateCode() {
        try {
            return getCode(System.currentTimeMillis());
        }
        catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCode(long timeMillis) throws InvalidKeyException, NoSuchAlgorithmException {
        return String.format(NUM_DIGITS_OUTPUT, getCode(new Base32().decode(sharedSecret), timeMillis));
    }

    private boolean verifyCode(String code)
            throws NoSuchAlgorithmException, InvalidKeyException {
        for (int i = -varianceSeconds; i <= varianceSeconds; i++) {
            final String testCode = getCode(System.currentTimeMillis());
            if (testCode.equals(code)) {
                return true;
            }
        }
        return false;
    }

    private long getCode(byte[] key, long timeMillis) throws NoSuchAlgorithmException, InvalidKeyException {

        byte[] data = new byte[8];
        long value = timeMillis / 1000 / timeStepSeconds;
        for (int i = 7; value > 0; i--) {
            data[i] = (byte) (value & 0xFF);
            value >>= 8;
        }

        // encrypt the data with the key and return the SHA1 of it in hex
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        // if this is expensive, could put in a thread-local
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);

        // take the 4 least significant bits from the encrypted string as an offset
        int offset = hash[hash.length - 1] & 0xF;

        // We're using a long because Java hasn't got unsigned int.
        long truncatedHash = 0;
        for (int i = offset; i < offset + 4; ++i) {
            truncatedHash <<= 8;
            // get the 4 bytes at the offset
            truncatedHash |= (hash[i] & 0xFF);
        }
        // cut off the top bit
        truncatedHash &= 0x7FFFFFFF;

        // the token is then the last 6 digits in the number
        truncatedHash %= 1000000;

        return truncatedHash;
    }

}
