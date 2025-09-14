package com.example.project;

import java.util.Arrays;
import java.util.List;

public class CaeserCipher {

    private static final List<Character> CHAR_SET = Arrays.asList(
            // lowercase
            'a','b','c','d','e','f','g','h','i','j','k','l','m',
            'n','o','p','q','r','s','t','u','v','w','x','y','z',
            'A','B','C','D','E','F','G','H','I','J','K','L','M',
            'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            '0','1','2','3','4','5','6','7','8','9',
            '!','@','#','$','%','^','&','*','(',')','-','_','=','+',
            '[',']','{','}',';',':','\'','"','<','>',',','.','?','/','|','\\'
    );

    // Encryption
    public static String encrypt(String plain, int shift) {
        StringBuilder cipher = new StringBuilder();
        for (char c : plain.toCharArray()) {
            if (CHAR_SET.contains(c)) {
                int pos = CHAR_SET.indexOf(c);
                int newPos = (pos + shift) % CHAR_SET.size();
                cipher.append(CHAR_SET.get(newPos));
            } else {
                cipher.append(c); // keep unchanged
            }
        }
        return cipher.toString();
    }

    // Decryption
    public static String decrypt(String cipher, int shift) {
        StringBuilder plain = new StringBuilder();
        for (char c : cipher.toCharArray()) {
            if (CHAR_SET.contains(c)) {
                int pos = CHAR_SET.indexOf(c);
                int newPos = (pos - shift + CHAR_SET.size()) % CHAR_SET.size();
                plain.append(CHAR_SET.get(newPos));
            } else {
                plain.append(c);
            }
        }
        return plain.toString();
    }
}
