package uz.lb.fxchatserver.crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESGCME {
    private static final int KEY_SIZE = 256;
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_SIZE = 12;

    public static void main(String[] args) throws Exception {
        String plainText = "Salom, dunyo!";

        // Kalit va IV yaratish
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(KEY_SIZE);
        SecretKey secretKey = keyGen.generateKey();

        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);

        // Shifrlash
        String encryptedText = encrypt(plainText, secretKey, iv);

        // Dekript qilish
        String decryptedText = decrypt(encryptedText, secretKey, iv);

        System.out.println("Dekript qilingan matn: " + decryptedText);
    }

    private static String encrypt(String plainText, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        // Natijani base64 formatda qaytaramiz
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private static String decrypt(String encryptedBase64, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);

        byte[] decodedEncrypted = Base64.getDecoder().decode(encryptedBase64);
        byte[] decryptedBytes = cipher.doFinal(decodedEncrypted);

        return new String(decryptedBytes);
    }
}
