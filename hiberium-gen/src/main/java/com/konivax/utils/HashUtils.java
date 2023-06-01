package com.konivax.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtils {

    private HashUtils() { }

    public static String fileCheckSum(String filePath) {
        try {
            byte[] data = Files.readAllBytes(Paths.get(filePath));
            return getCheckSum(data);
        } catch (IOException e) {
            return null;
        }
    }

    public static String getCheckSum(String data) {
        return getCheckSum(data.getBytes());
    }

    public static String getCheckSum(byte[] data) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(data);
            return new BigInteger(1, hash).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
