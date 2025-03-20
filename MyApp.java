package TextEncryption;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
// import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class MyApp {
    private static SecretKey savedKey; // Store the key for reuse

    // Generate a secret key for AES encryption
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // 128-bit key size
        return keyGen.generateKey();
    }

    // Encrypt text using AES
    public static String encrypt(String text, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(text.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt text using AES
    public static String decrypt(String encryptedText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            // Generate a secret key and save it
            if (savedKey == null) {
                savedKey = generateKey();
                System.out.println("Generated Key: " + Base64.getEncoder().encodeToString(savedKey.getEncoded()));
            }

            boolean exit = false;
            while (!exit) {
                // Ask the user to choose between encryption and decryption
                System.out.println("\nChoose an option:");
                System.out.println("1. Encrypt Text");
                System.out.println("2. Decrypt Text");
                System.out.println("3. Exit");
                System.out.print("Enter your choice (1, 2, or 3): ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        // Encrypt the text
                        System.out.print("Enter text to encrypt: ");
                        String originalText = scanner.nextLine();
                        String encryptedText = encrypt(originalText, savedKey);
                        System.out.println("Encrypted Text: " + encryptedText);
                        break;

                    case 2:
                        // Decrypt the text
                        System.out.print("Enter text to decrypt: ");
                        String encryptedInput = scanner.nextLine();
                        try {
                            String decryptedText = decrypt(encryptedInput, savedKey);
                            System.out.println("Decrypted Text: " + decryptedText);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Error: The input is not a valid Base64 encoded string.");
                        } catch (Exception e) {
                            System.out.println("Error: Decryption failed. The input may not be encrypted with the correct key.");
                        }
                        break;

                    case 3:
                        // Exit the program
                        System.out.println("Exiting the program. Goodbye!");
                        exit = true;
                        break;

                    default:
                        System.out.println("Invalid choice. Please choose 1, 2, or 3.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
