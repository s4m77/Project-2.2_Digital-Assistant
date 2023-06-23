package nlp;

import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Tester {

    public static void main(String[] args) throws IOException {
        Random r = new Random(42);
        //testSpellChecker(SpellCheck.Distance.QWERTY);

    }

    private static void testSpellChecker(SpellCheck.Distance type, Random r) throws IOException {
        List<String> words = getWords(1000);
        SpellCheck.setDistance(type);
        CSVWriter writer = new CSVWriter(new java.io.FileWriter("src/main/resources/experiments/QWERTY_output.csv"));
        System.out.println(words.size());
        // for each operation
        for (Operation op : Operation.values()) {
            // for each word
            int count = 0;
            String currWord = "";
            for (String word : words) {
                boolean cont = true;
                while (cont){
                    if (count == 0) {
                        currWord = word;
                    }
                    currWord = apply(currWord, op, r);
                    if (SpellCheck.correct(currWord).equals(word)) {
                        count++;
                    }
                    else {
                        cont = false;
                        System.out.println("Operation: " + op + " " + "Word: " + word + " " + "Count: " + count);
                        writer.writeNext(new String[]{op.toString(), word, Integer.toString(count)});
                        count = 0;
                    }
                }
            }
        }
        writer.close();
    }

    public enum Operation {
        REMOVE, ADD, SWAP
    }

    public static String apply(String s, Operation op, Random r) {
        return switch (op) {
            case REMOVE -> removeAtIndex(s, r);
            case ADD -> addAtIndex(s, r);
            case SWAP -> swapAtIndex(s, r);
            default -> throw new IllegalArgumentException("Unknown operation: " + op);
        };
    }

    public static List<String> getWords(int n){
        List<String> words = new ArrayList<>();
        try(BufferedReader r = new BufferedReader(new FileReader("src/main/resources/NLPdata/Corpus"))){
            String word;
            while ((word = r.readLine()) != null){
                if (word.length() > 4) {
                    words.add(word);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Collections.shuffle(words, new Random(42));
        return words.subList(0, n);
    }

    public static String removeAtIndex(String s, Random r){
        int index = r.nextInt(s.length());
        return s.substring(0, index) + s.substring(index + 1);
    }

    public static String addAtIndex(String s, Random r){
        int index = r.nextInt(s.length());
        char randomChar = (char) (r.nextInt(26) + 'a');
        return s.substring(0, index) + randomChar + s.substring(index);
    }

    public static String swapAtIndex(String s, Random r){
        int index = r.nextInt(s.length() - 1);
        return s.substring(0, index) + s.charAt(index + 1) + s.charAt(index) + s.substring(index + 2);
    }
}
