package com.healthcheck;

import org.jasypt.util.text.AES256TextEncryptor;

public class EncryptPassword {

    public static void main(String[] args) {

        AES256TextEncryptor encryptor =
                new AES256TextEncryptor();

        encryptor.setPassword("Put_MasterKey");// Your master key 

        String encrypted =
                encryptor.encrypt("Put_password"); // Put your password here to generate encrypted value

        System.out.println(encrypted);
    }
}