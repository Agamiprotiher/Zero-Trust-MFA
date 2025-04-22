package com.zerotrust.mfa.util;

import org.apache.commons.codec.binary.Base32;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.time.Instant;

public class TOTPUtil {

    // Configurable constants
    private static final int TIME_STEP_SECONDS = 30;
    private static final int OTP_DIGITS = 6;

    // Generate TOTP
    public static String generateTOTP(String base32Secret) {
        return generateOTP(base32Secret, getTimeWindow());
    }

    // Validate TOTP (with ±1 window leeway)
    public static boolean validateTOTP(String base32Secret, String userOTP) {
        long currentWindow = getTimeWindow();
        for (long i = -1; i <= 1; i++) {
            String validOTP = generateOTP(base32Secret, currentWindow + i);
            if (validOTP.equals(userOTP)) {
                return true;
            }
        }
        return false;
    }

    // Private: Generate OTP for a specific time window
    private static String generateOTP(String base32Secret, long timeWindow) {
        try {
            Base32 base32 = new Base32();
            byte[] key = base32.decode(base32Secret);

            byte[] timeBytes = ByteBuffer.allocate(8).putLong(timeWindow).array();

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            byte[] hash = mac.doFinal(timeBytes);

            int offset = hash[hash.length - 1] & 0xF;
            int binary = ((hash[offset] & 0x7F) << 24)
                       | ((hash[offset + 1] & 0xFF) << 16)
                       | ((hash[offset + 2] & 0xFF) << 8)
                       | (hash[offset + 3] & 0xFF);

            int otp = binary % (int) Math.pow(10, OTP_DIGITS);

            return String.format("%0" + OTP_DIGITS + "d", otp);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Error generating TOTP", e);
        }
    }


    // Private: Get current time window
    private static long getTimeWindow() {
        return Instant.now().getEpochSecond() / TIME_STEP_SECONDS;
    }
}




