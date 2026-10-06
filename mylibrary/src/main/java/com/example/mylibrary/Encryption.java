package com.example.mylibrary;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class Encryption {


    public static SecretKey getKey(String keyAlias) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            int nBefore = keyStore.size();
            Log.i("deff", nBefore + " size");

            return (SecretKey) keyStore.getKey(keyAlias, null);
        } catch (Exception e ) {
            e.printStackTrace();
        }
        return null;
    }
    public SecretKey generateKey(String keyAlias) {
        try {


            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES);
            keyGenerator.init(
                    new KeyGenParameterSpec.Builder(keyAlias,
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setKeySize(256)
                            .setRandomizedEncryptionRequired(false)
                            .build()
            );
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static SecretKey generateAndStoreKey(String keyAlias) {
        try {


            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(
                    new KeyGenParameterSpec.Builder(keyAlias,
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setKeySize(256)
                            .setRandomizedEncryptionRequired(false)
                            .build()
            );
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String generateIV() {
        var rand=new SecureRandom();
        var seed = rand.generateSeed(12);
        return Base64.encodeToString(seed, Base64.DEFAULT);
    }
    public static Cipher getCipher(SecretKey key, String iv, int cipherMode) {

        try {
            var cipher = Cipher.getInstance("AES/GCM/NoPadding");
            var parameterSpec = new javax.crypto.spec.GCMParameterSpec(128, Base64.decode(iv, Base64.DEFAULT));
            cipher.init(cipherMode, key, parameterSpec);
            return cipher;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String encrypt(SecretKey key, String iv, String text) {
        try {
            var cipher = getCipher(key, iv, Cipher.ENCRYPT_MODE);
            var bytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e){
            e.printStackTrace();

        }
        return null;
    }
    public static String decrypt(SecretKey key, String iv, String encryptedText) {
        try{
            var bytes = Base64.decode(encryptedText, Base64.DEFAULT);
            var cipher = getCipher(key, iv, Cipher.DECRYPT_MODE);
            var bytes_ = cipher.doFinal(bytes);
            return new String(bytes_, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }



    private static String PROVIDER = "AndroidKeyStore";
    private static String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BITS = 128; // Standard authentication tag length
    private static final int IV_LENGTH_BYTES = 12;  // Recommended IV length for GCM (96 bits)
    private static final int KEY_LENGTH_BITS = 256;  // Strongest AES key length


    public static SecretKey getOrGenerateKey(String keyAlias) {
        try {
            KeyStore keyStore = KeyStore.getInstance(PROVIDER);
            keyStore.load(null);

            int nBefore = keyStore.size();
            Log.i("deff", nBefore + " size");

            if (keyStore.containsAlias(keyAlias)) {
                return (SecretKey) keyStore.getKey(keyAlias, null);
            }


            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
                    PROVIDER);
            keyGenerator.init(
                    new KeyGenParameterSpec.Builder(keyAlias,
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            //.setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            //.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setKeySize(256)
                            //.setRandomizedEncryptionRequired(false)
                            .build()
            );
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey;
        } catch (Exception e ) {
            e.printStackTrace();
        }
        return null;
    }


    public static String encrypt(String text, SecretKey key) {
        try {
            byte[] plaintextBytes = text.getBytes(StandardCharsets.UTF_8);

            byte[] iv = new byte[IV_LENGTH_BYTES];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);

            // 2. Initialize the Cipher
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

            byte[] ciphertextWithTag = cipher.doFinal(plaintextBytes);

            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + ciphertextWithTag.length);
            byteBuffer.put(iv);
            byteBuffer.put(ciphertextWithTag);
            byte[] encryptedMessage = byteBuffer.array();

            // 5. Encode to Base64 string for safe transport/storage
            return Base64.encodeToString(encryptedMessage, Base64.DEFAULT);
        } catch (Exception e){
            e.printStackTrace();

        }
        return null;
    }
    public static String decrypt(String encryptedText, SecretKey key) {
        try{
            var bytes = Base64.decode(encryptedText, Base64.DEFAULT);
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            byte[] iv = new byte[IV_LENGTH_BYTES];
            byteBuffer.get(iv);

            byte[] ciphertextWithTag = new byte[byteBuffer.remaining()];
            byteBuffer.get(ciphertextWithTag);

            // 4. Initialize the Cipher for Decryption
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

            // 5. Decrypt and verify the authentication tag
            byte[] plaintextBytes = cipher.doFinal(ciphertextWithTag);

            return new String(plaintextBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
