package com.sturent.util;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.util.Base64;

public class PasswordUtil {
    private static final int SALT_LENGTH=16;

    public static byte[] generateSalt(){

        byte[]salt=new byte[SALT_LENGTH];
        SecureRandom random=new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String password,byte[] salt)
                                                   throws GeneralSecurityException{

                                  PBEKeySpec spec=new PBEKeySpec(password.toCharArray(),salt,65536,256);



    SecretKeyFactory factory=SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

    byte[] hash=factory.generateSecret(spec).getEncoded();

    return Base64.getEncoder().encodeToString(hash);
    }


    public static boolean verifyPassword(String password,String storedHash,byte[]salt)
                                  throws GeneralSecurityException{
                                                           String hash=hashPassword(password,salt);
                                                           return hash.equals(storedHash);
    }




}











