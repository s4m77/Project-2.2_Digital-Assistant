package nlp;

import nlp.distance.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class SpellCheck {

    public enum Distance {
        EDIT, COSINE, HAMMING, JARO, QWERTY
    }

    private static final Trie trie = new Trie();
    private static final Map<String, Integer> dict = new HashMap<>();
    public static Distance type = null;


    public static void main(String[] args) throws IOException {
        String text = "what is lectur on Moday";

        SpellCheck.setDistance(Distance.QWERTY);
        System.out.println(SpellCheck.correctSentence(text));
    }

    public static void setDistance(Distance type){
        SpellCheck.type = type;
    }

    public static double getDistance(String str1, String str2){
        return switch (type){
            case EDIT -> MinEdit.calculate(str1, str2);
            case HAMMING -> Hamming.calculate(str1, str2);
            case JARO -> Jaro.calculate(str1, str2);
            case QWERTY -> Qwerty.calculate(str1, str2);
            case COSINE -> Cosine.calculate(str1, str2);
        };
    }

    public static String correctSentence(String input) throws IOException {
        String[] words = input.split("\\s");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            String correctedWord = correct(word);
            sb.append(correctedWord).append(" ");
        }
        return sb.toString().trim();
    }

    public static String correct(String input) throws IOException {
        String fileName = "src/main/resources/NLPdata/Corpus";
        List<String> corpus =  Files.readAllLines(Paths.get(fileName));;
        SpellCheck.init(corpus);
        return SpellCheck.bestMatch(input);
    }

    public static void init(List<String> dictionary){
        for(String line: dictionary){
            String word = line.toLowerCase();
            if (!line.contains(" ")) {
                dict.put(word, dict.getOrDefault(word, 0)+1);
                trie.add(word);
            } else {
                String[] strs= line.split("\\s");
                for(String str: strs) {
                    dict.put(str, dict.getOrDefault(str, 0)+1);
                    trie.add(str);
                }
            }
        }
    }

    public static String bestMatch(String inputWord) {
        String s = inputWord.toLowerCase();
        String res;
        TreeMap<Double, TreeMap<Double, TreeSet<String>>> map = new TreeMap<>();
        TrieNode node = trie.find(s);
        if(node == null) {
            for (String w: dict.keySet()) {

                double dist = getDistance(w, s);
                TreeMap<Double, TreeSet<String>> similarWords = map.getOrDefault(dist, new TreeMap<>());
                double freq = dict.get(w);
                if (w.equals("deepSpace")) {
                    freq = 10; // Set the weight of 'deepSpace' to 10
                } else if (w.equals("spaceBox")) {
                    freq = 10; // Set the weight of 'spaceBox' to 10
                }else if (w.equals("wiki")) {
                    freq = 10; // Set the weight of 'wiki' to 10
                }else if (w.equals("weather")) {
                    freq = 10; // Set the weight of 'weather' to 10
                }else if (w.equals("which")) {
                    freq = 10; // Set the weight of 'which' to 10
                }else if (w.equals("lectures")) {
                    freq = 10; // Set the weight of 'lectures' to 10
                }else if (w.equals("are")) {
                    freq = 10; // Set the weight of 'are' to 10
                }else if (w.equals("there")) {
                    freq = 10; // Set the weight of 'there' to 10
                }else if (w.equals("what")) {
                    freq = 10; // Set the weight of 'what' to 10
                }else if (w.equals("is")) {
                    freq = 10; // Set the weight of 'is' to 10
                }else if (w.equals("sittard")) {
                    freq = 10; // Set the weight of 'sittard' to 10
                }else if (w.equals("heerlen")) {
                    freq = 10; // Set the weight of 'heerlen' to 10
                }else if (w.equals("maastricht")) {
                    freq = 10; // Set the weight of 'maastricht' to 10
                }else if (w.equals("dialing")) {
                    freq = 10; // Set the weight of 'dialing' to 10
                }else if (w.equals("monday")) {
                    freq = 10; // Set the weight of 'is' to 10
                }else if (w.equals("tuesday")) {
                    freq = 10; // Set the weight of 'is' to 10
                }else if (w.equals("wednesday")) {
                    freq = 10; // Set the weight of 'is' to 10
                }else if (w.equals("thursday")) {
                    freq = 10; // Set the weight of 'is' to 10
                }else if (w.equals("friday")) {
                    freq = 10; // Set the weight of 'is' to 10
                }else if (w.equals("saturday")) {
                    freq = 10; // Set the weight of 'is' to 10
                }
                TreeSet<String> set = similarWords.getOrDefault(freq, new TreeSet<>());
                set.add(w);
                similarWords.put(freq, set);
                map.put(dist, similarWords);
            }
            res = map.firstEntry().getValue().lastEntry().getValue().first();
        } else {
            res = s;
        }
        return res;
    }

}
