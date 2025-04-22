package com.zerotrust.mfa;

import com.zerotrust.mfa.util.TOTPUtil;
import com.zerotrust.mfa.util.QRCodeUtil;

import java.util.Scanner;

public class ZeroTrustMFAApplication {
    public static void main(String[] args) {
        String base32Secret = "MZXW6YTBOI======";

        // TOTP generate karo
        String otp = TOTPUtil.generateTOTP(base32Secret);
        System.out.println("Your current TOTP is: " + otp);

        // QR Code generate karo
        String otpAuthURL = "otpauth://totp/ZeroTrustMFA:unicorn?secret=" + base32Secret + "&issuer=ZeroTrustMFA";
        QRCodeUtil.generateQRCode(otpAuthURL, "qrcode.png", 300, 300);
        System.out.println("QR Code generated: qrcode.png");

        // User se OTP input le lo
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter OTP from Google Authenticator: ");
        String userInput = scanner.nextLine();

        // Validate karo
        if (userInput.equals(otp)) {
            System.out.println("✅ OTP Verified Successfully!");
        } else {
            System.out.println("❌ Invalid OTP!");
        }
    }
}







