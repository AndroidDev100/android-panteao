package panteao.make.ready.utils.security;


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CryptUtil {

    private static CryptUtil cryptUtil;
    private static SecretKeySpec secretKey;
    private static final String ENCODING_METHOD = "AES/ECB/PKCS5Padding";
    private static byte[] key;
    private static final String CHARACTER_SET = "UTF-8";
    private static final String ALGORITHM = "AES";

    public static CryptUtil getInstance() {
        if (cryptUtil == null) {
            cryptUtil = new CryptUtil();
        }
        return cryptUtil;
    }

    public static void setKey(String myKey) {
        MessageDigest sha;
        try {
            key = myKey.getBytes(CHARACTER_SET);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String strToEncrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance(ENCODING_METHOD);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes(CHARACTER_SET)), 1);

        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public String decrypt(String strToDecrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance(ENCODING_METHOD);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.decode(strToDecrypt, 1)));

        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
