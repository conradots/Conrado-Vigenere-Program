import java.util.*;
import edu.duke.*;
import java.io.*;

public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        StringBuilder slice = new StringBuilder();
        for (int i = whichSlice; i < message.length(); i += totalSlices) {
            slice.append(message.charAt(i));
        }
        //System.out.println(allSliceChars);
        return slice.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        CaesarCracker caesarCracker = new CaesarCracker(mostCommon);
        for (int i = 0; i < klength; i++) {
            String slice = sliceString(encrypted, i, klength);
            key[i] = caesarCracker.getKey(slice);
        }
        /*
        for(int j = 0; j < key.length; j++) {
            System.out.print(key[j] + " ");
        }
        */
        return key;
    }
    
    public HashSet<String> readDictionary(FileResource fr) {
        HashSet<String> dictionary = new HashSet<String>();
        for (String line : fr.lines()) {
            line = line.toLowerCase();
            dictionary.add(line);
        }
        return dictionary;
    }
    
    public int countWords(String message, HashSet<String> dictionary) {
        int count = 0;
        String[] words = message.split("\\W+");
        for (String word : words) {
            if (dictionary.contains(word.toLowerCase())) {
                count += 1;
            }
        }
        return count;
    }
    
    public String breakForLanguage(String encrypted, HashSet<String> dictionary) {
        String bestDecrypted = "";
        int bestKeyLength = 0;
        int maxCount = 0; // max count of real words
        char mostCommon = mostCommonCharIn(dictionary);
        for (int i = 1; i <= 100; i++) {
            int[] key = tryKeyLength(encrypted, i, mostCommon);
            VigenereCipher vc = new VigenereCipher(key);
            String decrypted = vc.decrypt(encrypted);
            int count = countWords(decrypted, dictionary);
            if (count > maxCount) {
                maxCount = count;
                bestKeyLength = i;
                bestDecrypted = decrypted;
            }
        }
        System.out.println("Key length: " + bestKeyLength);
        System.out.println("Valid words: " + maxCount);
        System.out.println("\n");
        return bestDecrypted;
    }
    
    public char mostCommonCharIn(HashSet<String> dictionary) {
        char mostCommon = '\u0000';
        int mostFreq = 0;
        HashMap<Character, Integer> charFreqs = new HashMap<Character, Integer>();
        for (String word : dictionary) {
            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
                if (!charFreqs.containsKey(letter)) {
                    charFreqs.put(letter, 1);
                } else {
                    charFreqs.put(letter, charFreqs.get(letter) + 1);
                }
            }
        }
        for (char currLetter : charFreqs.keySet()) {
            int currFreq = charFreqs.get(currLetter);
            if (currFreq > mostFreq) {
                mostFreq = currFreq;
                mostCommon = currLetter;
            }
        }
        return mostCommon;
    }
    
    public String breakForAllLangs(String encrypted, HashMap<String, HashSet<String>> languages) {
        String bestDecrypted = "";
        String bestLanguage = "";
        int maxCount = 0;
        for (String language : languages.keySet()) {
            HashSet<String> dictionary = languages.get(language);
            String decrypted = breakForLanguage(encrypted, dictionary);
            int count = countWords(decrypted, dictionary);
            if (count > maxCount) {
                maxCount = count;
                bestLanguage = language;
                bestDecrypted = decrypted;
            }
        }
        System.out.println("Final result:");
        System.out.println("Language: " + bestLanguage);
        System.out.println("Valid words: " + maxCount);
        System.out.println("\n");
        return bestDecrypted;
    }
    
    public void breakVigenere () {
        // Get an encrypted message
        FileResource fr = new FileResource();
        String encrypted = fr.asString();
        // Get all dictionaries
        DirectoryResource dictFiles = new DirectoryResource();
        //HashSet<String> dictionary = readDictionary(dictFile);
        HashMap<String, HashSet<String>> dictionaries = new HashMap<String, HashSet<String>>();
        for (File dictFile : dictFiles.selectedFiles()) {
            System.out.println("Processing the dictionary...");
            String language = dictFile.getName();
            FileResource currDict = new FileResource(dictFile);
            HashSet<String> dictionary = readDictionary(currDict);
            dictionaries.put(language, dictionary);
        }
        // Decrypt the encrypted message
        String decrypted = breakForAllLangs(encrypted, dictionaries);
        System.out.println(decrypted);
    }
}