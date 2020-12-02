package hr.fer.oprpp1.hw05.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.spec.AlgorithmParameterSpec;

public class Crypto {
    public static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    public static byte[] digest(InputStream input) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    public static void encrypt(InputStream input, OutputStream output, SecretKeySpec keySpec, AlgorithmParameterSpec paramSpec) {
        crypt(Cipher.ENCRYPT_MODE, input, output, keySpec, paramSpec);
    }

    public static void decrypt(InputStream input, OutputStream output, SecretKeySpec keySpec, AlgorithmParameterSpec paramSpec) {
        crypt(Cipher.DECRYPT_MODE, input, output, keySpec, paramSpec);
    }

    private static void crypt(int mode, InputStream input, OutputStream output, SecretKeySpec keySpec, AlgorithmParameterSpec paramSpec) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }
}
