package nlp;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import nlp.SpellCheck.*;
import java.io.IOException;
import java.util.*;

public class SpellCheckTest {
    private static final Random random = new Random();

    public static String modifyString(String input) throws IOException {
        int modification = random.nextInt(4); // random int for each case of modification

        String modified = "";
        switch (modification) {
            case 0:
                modified = deleteWord(input);
                break;
            case 1:
                modified = insertWord(input); // todo sam
                break;
            case 2:
                modified = substituteWord(input); // todo sam
                break;
            case 3:
                modified = swapWords(input);
                break;
        }
        System.out.println("Modification made: " + modification);
        return modified;
    }

    private static String deleteWord(String input) {
        String[] words = input.split("\\s");
        int index = random.nextInt(words.length);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (i == index) {
                continue;
            }
            sb.append(words[i]).append(" ");
        }
        return sb.toString().trim();
    }


    private static String insertWord(String input) throws IOException {
        String[] words = input.split("\\s");
        int index = random.nextInt(words.length);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            sb.append(words[i]).append(" ");
            if (i == index) {
                sb.append(getRandomWord()).append(" ");
            }
        }
        return sb.toString().trim();
    }

    /**
     * This method inserts a random character into the input string
     * @param input String to be modified
     * @return modified string
     */
    private static String insertCharInWord(String input){
        // select random index of char in word
        int index = random.nextInt(input.length());
        // insert a random char at that index
        char randomChar = (char) (random.nextInt(26) + 'a');
        return input.substring(0, index) + randomChar + input.substring(index);
    }

    /**
     * This method replaces a random character in the input string
     * @param input String to be modified
     * @return modified string
     */
    private static String replaceCharInWord(String input){
        // select random index of char in word
        int index = random.nextInt(input.length());
        // insert a random char at that index
        char randomChar = (char) (random.nextInt(26) + 'a');
        return input.substring(0, index) + randomChar + input.substring(index + 1);
    }

    private static String substituteWord(String input) throws IOException {
        String[] words = input.split("\\s");
        int index = random.nextInt(words.length);
        words[index] = getRandomWord();
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word).append(" ");
        }
        return sb.toString().trim();
    }

    private static String swapWords(String input) {
        String[] words = input.split("\\s");
        int index1 = random.nextInt(words.length);
        int index2 = random.nextInt(words.length);
        String temp = words[index1];
        words[index1] = words[index2];
        words[index2] = temp;
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word).append(" ");
        }
        return sb.toString().trim();
    }

    private static String getRandomWord() throws IOException {
        String fileName = "src/main/resources/NLPdata/Corpus";
        List<String> corpus =  Files.readAllLines(Paths.get(fileName));;
        // Convert corpus into array of strings
        String[] words = corpus.toArray(new String[corpus.size()]);
        return words[random.nextInt(words.length)];
    }

    public static void main(String[] args) throws IOException {
//        String text = "I like to eats apple and banana"; // 20 words
//
//        int numTests = 10;
//        int numSimilar = 0;
//        for (int i = 0; i < numTests; i++) {
//            String modified = SpellCheckTest.modifyString(text);
//            String expected = SpellCheck.correctSentence(text);
//            String actual = SpellCheck.correctSentence(modified);
//            if (actual.equals(expected)) {
//                numSimilar++;
//            }
//            System.out.println("Current test:" + i + " *** Nbcorrect : " + numSimilar + " *** Modified sentence: " + modified + " *** Operation performed:");
//        }
//        double percentageCorrect = ((double) numSimilar / numTests) * 100;
//        System.out.println("Percentage correct: " + percentageCorrect + "%");

        for (int i = 0; i < 1000; i++) {
            System.out.println("Inserting: "+ insertCharInWord("hello"));
            System.out.println("Replacing: "+ replaceCharInWord("hello"));

        }
    }

}
