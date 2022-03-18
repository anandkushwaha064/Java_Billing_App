package org.billing.utility;

import org.billing.configuration.EnvironmentConfig;
import org.billing.pages.MessageScreen;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;


public class CryptoUtils {
    private static SecretKey key;

    private static void setKey() {

        String encryptionKeyStr = EnvironmentConfig.encryptionKey;
        if (!CommonUtils.isValidString(encryptionKeyStr))
            throw new RuntimeException("Make sure you have set the 'EncryptionKey' in the EnvironmentConfig. ");
        if (key == null) {
            try {
                DESKeySpec keySpec = new DESKeySpec(encryptionKeyStr.getBytes(StandardCharsets.UTF_8));
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                key = keyFactory.generateSecret(keySpec);
            } catch (Exception e) {
                throw new RuntimeException("Error occurred while creating the key instance of CryptoUtils. ", e);
            }
        }
    }


    public static String encryptTheValue(String plainText) throws Exception {
        setKey();
        Base64.Encoder base64encoder = Base64.getEncoder();
        byte[] cleartext = plainText.getBytes(StandardCharsets.UTF_8);
        Cipher cipher = Cipher.getInstance("DES");

        cipher.init(Cipher.ENCRYPT_MODE, key);
        return base64encoder.encodeToString(cipher.doFinal(cleartext));
    }

    public static String decryptTheValue(String encryptedTextInput) throws Exception {
        setKey();
        if (!CommonUtils.isValidString(encryptedTextInput))
            MessageScreen.showException("Encrypted Text Input to be enter should not be null or empty");

        Base64.Decoder base64decoder = Base64.getDecoder();
        byte[] encryptedBytes = base64decoder.decode(encryptedTextInput);
        Cipher cipher1 = Cipher.getInstance("DES");
        cipher1.init(Cipher.DECRYPT_MODE, key);
        byte[] plainTextPwdBytes = (cipher1.doFinal(encryptedBytes));
        return new String(plainTextPwdBytes);
    }

    /**
     * This method should be called from command line
     * This method used to encrypt the new value
     */
//    public static void main(String[] args) throws Exception {
//        System.out.print("\n\nEncrypt value - 1\nDecrypt value - 2\nEnter You choice : ");
//        Scanner scanner = new Scanner(System.in);
//        String value = scanner.nextLine();
//
//        if(value.equals("1")) {
//            System.out.print("Enter the value that has to be encrypted : ");
//            String plainText = scanner.nextLine();
//            System.out.println("Encrypted value : " + CryptoUtils.encryptTheValue(plainText));
//        }
//        else if(value.equals("2"))
//        {
//            System.out.print("Enter the value that has to be decrypted : ");
//            String plainText = scanner.nextLine();
//            System.out.println("Decrypted value : " + CryptoUtils.decryptTheValue(plainText));
//        }
//
//    }
}
