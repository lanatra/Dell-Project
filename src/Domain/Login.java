package Domain;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by Andreas Poulsen on 09-Apr-15.
 */
public class Login {

    public static String createPassword(String pw) {
        // Generate Salt
        Random r = new SecureRandom();
        byte[] s = new byte[6];
        r.nextBytes(s);
        String salt = byteArrayToString(s);

        String password = salt + "$" + stringToHash(salt+pw);

        return password;
    }

    public static boolean testPassword(String pw, String saltedpw) {
        String salt = saltedpw.substring(0, 12);
        return saltedpw.equals(salt + "$" + stringToHash(salt + pw));
    }

    public static String byteArrayToString(byte[] a) {
        char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a) {
            sb.append(HEX_CHARS[(b & 0xF0) >> 4]);
            sb.append(HEX_CHARS[b & 0x0F]);
        }
        return sb.toString();
    }

    public static String stringToHash(String s) {
        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(s.getBytes("UTF-8"));
        } catch (Exception E) {};

        return byteArrayToString(hash);
    }
}
