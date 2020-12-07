package hr.fer.oprpp1.hw05.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Scanner;

/**
 * A program used to check a file's SHA-256 digest or to en- and decrypt it using AES.
 *
 * @author Borna Cafuk
 */
public class Crypto {
    /**
     * How many bytes are read from the file at a time in {@link #feedFile(InputStream, IOConsumer)}.
     */
    private static final int BUFFER_SIZE = 4096;
    /**
     * The message printed when the program arguments don't match what is expected.
     */
    private static final String USAGE_MESSAGE = """
            Usage: Crypto mode params ...

            Modes:
             checksha file                         Check the SHA-256 digest of a file
             encrypt input_file output_file        Encrypt a file using the AES algorithm
             decrypt input_file output_file        Decrypt a file using the AES algorithm""";

    /**
     * The main entry point into the program.
     *
     * @param args command line arguments, see {@link #USAGE_MESSAGE}
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println(USAGE_MESSAGE);
            System.exit(1);
        }

        String mode = args[0];

        try {
            switch (mode) {
                case "checksha" -> checkshaMain(args);
                case "encrypt" -> cryptMain(args, true);
                case "decrypt" -> cryptMain(args, false);
                default -> throw new CryptoException("Unknown mode " + mode + "\n" + USAGE_MESSAGE);
            }
        } catch (CryptoException e) {
            System.err.println(e.getMessage());

            Throwable cause = e.getCause();
            if (cause != null)
                System.err.println(cause.toString());

            System.exit(1);
        }
    }

    /**
     * An interactive command line program to validate a file's SHA-256 digest.
     *
     * @param args command line arguments, see {@link #USAGE_MESSAGE}
     * @throws CryptoException if the check could not be performed for any reason
     */
    private static void checkshaMain(String[] args) {
        if (args.length != 2)
            throw new CryptoException("Expected 1 parameter, but got " + (args.length - 1) + "\n" + USAGE_MESSAGE);

        String inputFileName = args[1];
        Path inputFile = Paths.get(inputFileName);

        try (InputStream input = Files.newInputStream(inputFile)) {

            String expectedDigestHex;
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Please provide expected sha-256 digest for " + inputFileName + ":\n> ");

                if (!scanner.hasNextLine())
                    throw new CryptoException("No digest was provided.");
                expectedDigestHex = scanner.nextLine().trim();
            }

            byte[] expectedDigest;
            try {
                expectedDigest = Util.hextobyte(expectedDigestHex);
            } catch (IllegalArgumentException e) {
                throw new CryptoException("The digest provided is not valid", e);
            }

            byte[] actualDigest = digest(input);
            System.out.print("Digesting completed. ");

            boolean digestsEqual = Arrays.equals(expectedDigest, actualDigest);

            if (digestsEqual)
                System.out.println("Digest of " + inputFileName + " matches expected digest.");
            else
                System.out.println("Digest of " + inputFileName + " does not match the expected digest. Digest was: " + Util.bytetohex(actualDigest));

        } catch (IOException e) {
            throw new CryptoException("IO error", e);
        }
    }

    /**
     * An interactive command line program to encrypt or decrypt a file using AES.
     *
     * @param args    command line arguments, see {@link #USAGE_MESSAGE}
     * @param encrypt if {@code true}, the function will encrypt; if {@code false}, the function will decrypt
     * @throws CryptoException if the en- or decryption could not be performed for any reason
     */
    private static void cryptMain(String[] args, boolean encrypt) {
        if (args.length != 3)
            throw new CryptoException("Expected 2 parameters, but got " + (args.length - 1) + "\n" + USAGE_MESSAGE);

        String inputFileName = args[1];
        String outputFileName = args[2];

        Path inputFile = Paths.get(inputFileName);
        Path outputFile = Paths.get(outputFileName);

        try (InputStream input = Files.newInputStream(inputFile);
             OutputStream output = Files.newOutputStream(outputFile)
        ) {
            byte[] password;
            byte[] iv;
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n> ");

                if (!scanner.hasNextLine())
                    throw new CryptoException("No password was provided.");
                String passwordHex = scanner.nextLine().trim();
                if (passwordHex.length() != 32)
                    throw new CryptoException("The password length should be 32 characters, but " + passwordHex.length() + " were provided.");

                try {
                    password = Util.hextobyte(passwordHex);
                } catch (IllegalArgumentException e) {
                    throw new CryptoException("The password provided is not valid", e);
                }

                System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):\n> ");

                if (!scanner.hasNextLine())
                    throw new CryptoException("No initialization vector was provided.");
                String ivHex = scanner.nextLine().trim();
                if (ivHex.length() != 32)
                    throw new CryptoException("The initialization vector length should be 32 characters, but " + ivHex.length() + " were provided.");

                try {
                    iv = Util.hextobyte(ivHex);
                } catch (IllegalArgumentException e) {
                    throw new CryptoException("The initialization vector provided is not valid", e);
                }
            }

            crypt(encrypt, input, output, password, iv);

            System.out.print("Encryption completed. Generated file " + outputFileName + " based on file " + inputFileName + ".");
        } catch (IOException e) {
            throw new CryptoException("IO error", e);
        }
    }

    /**
     * Calculates the SHA-256 digest for an input stream.
     *
     * @param input the input stream whose digest to calculate
     * @return the digest
     * @throws IOException     if the stream could not be read from
     * @throws CryptoException if SHA-256 is not available in the environment
     */
    public static byte[] digest(InputStream input) throws IOException {
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException("Could not instantiate MessageDigest", e);
        }

        feedFile(input, sha::update);

        return sha.digest();
    }

    /**
     * En- or decrypts a stream and writes the result to another stream.
     *
     * @param encrypt  if {@code true}, the function will encrypt; if {@code false}, the function will decrypt
     * @param input    the input stream to read from
     * @param output   the output stream to write to
     * @param password the key to use
     * @param iv       the initialization vector to use
     * @throws IOException     if {@code input} could not be read from, or if {@code output} could not be written to
     * @throws CryptoException if AES is not available in the environment
     * @throws CryptoException if the password or initialization vector is invalid
     */
    private static void crypt(boolean encrypt, InputStream input, OutputStream output, byte[] password, byte[] iv) throws IOException {
        SecretKeySpec keySpec = new SecretKeySpec(password, "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new CryptoException("Could not instantiate Cipher", e);
        }
        try {
            cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new CryptoException("Could not initialize Cipher", e);
        }

        feedFile(input, buffer -> output.write(cipher.update(buffer)));

        try {
            output.write(cipher.doFinal());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new CryptoException("Encryption error", e);
        }
    }

    /**
     * Reads a stream in blocks of {@link #BUFFER_SIZE} bytes and feeds it to a {@link IOConsumer}.
     * <p>
     * The parameter of {@code consumer} will be a {@code byte[]} containing the data read.
     *
     * @param input    the stream to read from
     * @param consumer a consumer invoked with data read from the stream
     * @throws IOException if reading from {@code input} fails or if {@code consumer} throws an {@link IOException}
     */
    private static void feedFile(InputStream input, IOConsumer<byte[]> consumer) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        while (true) {
            int bytesRead = input.read(buffer);

            if (bytesRead == -1)
                break;

            byte[] data = Arrays.copyOf(buffer, bytesRead);
            consumer.accept(data);
        }
    }

    /**
     * Represents an operation that accepts a single input argument and returns no result.
     * Unlike {@link java.util.function.Consumer}, {@code IOConsumer} is meant to be able to IO operations and
     * may throw an {@link IOException}.
     *
     * @param <T> the type of the input to the operation
     */
    @FunctionalInterface
    private interface IOConsumer<T> {
        /**
         * Performs this operation on the given argument.
         *
         * @param t the input argument
         * @throws IOException if the operation needs to perform IO and fails
         */
        void accept(T t) throws IOException;
    }
}
