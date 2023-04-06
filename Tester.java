/**
 * Write a description of Tester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import edu.duke.*;

public class Tester {
    public void testCaesarCipher() {
        FileResource fr = new FileResource("./VigenereTestData/titus-small.txt");
        CaesarCipher caesarCipher = new CaesarCipher(10);
        
        String message = fr.asString();
        System.out.println(message);
        
        String encrypted = caesarCipher.encrypt(message);
        System.out.println(encrypted);
        
        String decrypted = caesarCipher.decrypt(encrypted);
        System.out.println(decrypted);
    }
    
    public void testCaesarCracker() {
        // Test 1
        System.out.println("Test 1");
        FileResource fr1 = new FileResource("./VigenereTestData/titus-small_key5.txt");
        String message1 = fr1.asString();
        CaesarCracker caesarCracker1 = new CaesarCracker();
        
        String decrypted1 = caesarCracker1.decrypt(message1);
        System.out.println(decrypted1);
        
        // Test 2
        System.out.println("\n\nTest 2");
        FileResource fr2 = new FileResource("./VigenereTestData/oslusiadas_key17.txt");
        String message2 = fr2.asString();
        CaesarCracker caesarCracker2 = new CaesarCracker('a');
        
        String decrypted2 = caesarCracker2.decrypt(message2);
        System.out.println(decrypted2);
    }
    
    public void testVigenereCipher() {
        FileResource fr = new FileResource("./VigenereTestData/titus-small.txt");
        String message = fr.asString();
        int[] key = {17, 14, 12, 4};
        VigenereCipher vigenereCipher = new VigenereCipher(key);
        
        String encrypted = vigenereCipher.encrypt(message);
        System.out.println(encrypted);
        
        String decrypted = vigenereCipher.decrypt(encrypted);
        System.out.println(decrypted);
    }
    
    public void testVigenereBreaker() {
        //FileResource fr = new FileResource("./VigenereTestData/athens_keyflute.txt");
        FileResource fr = new FileResource("./messages/secretmessage1.txt");
        //FileResource fr = new FileResource("./messages/secretmessage2.txt");
        String message = fr.asString();
        VigenereBreaker vigenereBreaker = new VigenereBreaker();
        int[] key = vigenereBreaker.tryKeyLength(message, 4, 'e');
        
        /* Attention: In Java, you cannot directly print a whole array using System.out.print.
           You need to loop through that array and print each element one by one. */
        for(int j = 0; j < key.length; j++) {
            System.out.print(key[j] + " ");
        }
    }
}