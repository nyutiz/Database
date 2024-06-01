package com.nyutiz;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class Crypter {
    public static String encrypt(String input, int key) {
        char[] chars = input.toCharArray();
        StringBuilder encryptedString = new StringBuilder();

        for (char c : chars) {
            c += key;
            encryptedString.append(c);
        }

        return encryptedString.toString();
    }


    public static String decrypt(String input, int key) {
        char[] chars = input.toCharArray();
        StringBuilder decryptedString = new StringBuilder();

        for (char c : chars) {
            c -= key;
            decryptedString.append(c);
        }

        return decryptedString.toString();
    }

    public static void decryptFile(String filePath, int key){
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            char[] chars = content.toCharArray();
            StringBuilder decryptedString = new StringBuilder();

            for (char c : chars) {
                c -= key;
                decryptedString.append(c);
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(String.valueOf(decryptedString));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void encryptFile(String filePath, int key){
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            char[] chars = content.toCharArray();
            StringBuilder decryptedString = new StringBuilder();

            for (char c : chars) {
                c += key;
                decryptedString.append(c);
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(String.valueOf(decryptedString));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static int secureKey(String password){
        int key = 0;
        Random random = new Random();
        int randomNumber = random.nextInt(100) + 1;

        int passwordTotal = 0;
        for (int i = 0; i < password.length(); i++) {
            int asciiValue = password.charAt(i);
            passwordTotal = passwordTotal + asciiValue;
        }
        key = randomNumber + passwordTotal;
        System.out.println(key);
        return key;
    }
}
