package nlp;

import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static gui.utils.PyCaller.startServer;
import static nlp.PredictAction.predictAction;

public class Tester {

    public static void main(String[] args) throws IOException {
        //testSpellChecker(SpellCheck.Distance.QWERTY);

        Random r = new Random(42);
        startServer();
        CSVWriter writer = new CSVWriter(new FileWriter("src/main/resources/experiments/NLP_output.csv"));
        ArrayList<String> rawSentences = readData("src/main/resources/mmda_nlp/data/training.csv", "sentence");
        ArrayList<String> sentences = new ArrayList<>(rawSentences.subList(0, 416));

        // for each operation
        for (Operation op: Operation.values()){
            // for each sentence
            for (String s: sentences){
                int count = 0;
                String currS = s;
                String trueLabel = predictAction(s);
                boolean cont = true;
                // while the label is correct count the number of operations
                while(cont){
                    count++;
                    currS = apply(currS, op, r);
                    String predicted = predictAction(currS);
                    cont = predicted.equals(trueLabel);
                }
                writer.writeNext(new String[]{op.toString(), s, Integer.toString(count)});
                System.out.println("Operation: " + op + " " + "Sentence: " + s + " " + "Count: " + count);
            }
        }
        writer.flush();
        writer.close();
    }

    private static void testSpellChecker(SpellCheck.Distance type, Random r) {
        try{
            List<String> words = getWords(1000);
            SpellCheck.setDistance(type);
            CSVWriter writer = new CSVWriter(new FileWriter("src/main/resources/experiments/QWERTY_output.csv"));
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
        } catch (IOException e) {
            System.err.println("Error in writing results");;
        }

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

    public static ArrayList<String> readData(String csvFile, String column) {
        ArrayList<String> sentences = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            String[] headers = null;
            int columnIndex = -1;

            if ((line = br.readLine()) != null ) {
                headers = line.split(",");
                for (int i = 0; i < headers.length; i++) {
                    if (headers[i].equals(column)) {
                        columnIndex = i;
                        break;
                    }
                }
            }

            if (columnIndex == -1) {
                System.out.println("Column '" + column + "' not found in the CSV file.");
                return sentences;
            }

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (columnIndex < values.length) {
                    sentences.add(values[columnIndex]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sentences;
    }
}
