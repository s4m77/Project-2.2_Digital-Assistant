package nlp;

import nlp.distance.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;



public class SpellCheck {

    public static final String PATH = System.getProperty("user.dir")+"/src/main/resources/NLPdata/";
    private String fileName;
    public final Distance type;

    public enum Distance {
        EDIT, HAMMING, JARO, QWERTY, COSINE
    }

    private final Trie trie = new Trie();
    private final Map<String, Integer> dict = new HashMap<>();

    //constructor
    public SpellCheck(String fileName, Distance type){
        this.type = type;
        try{
            this.fileName = PATH+fileName;
            List<String> corpus =  Files.readAllLines(Paths.get(fileName));;
            this.init(corpus);
        } catch (IOException e){
            System.out.println("File not found");
        }
    }

    public void init(List<String> dictionary){
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

    public String correctSentence(String input) throws IOException {
        String[] words = input.split("\\s");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            String correctedWord = correct(word);
            sb.append(correctedWord).append(" ");
        }
        return sb.toString().trim();
    }

    public String correct(String input) throws IOException {
        //this.fileName = "src/main/resources/NLPdata/Corpus";
        List<String> corpus =  Files.readAllLines(Paths.get(this.fileName));;
        this.init(corpus);
        return this.bestMatch(input);
    }



    public String bestMatch(String inputWord) {
        String s = inputWord.toLowerCase();
        String res;
        TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> map = new TreeMap<>();
        TrieNode node = trie.find(s);
        if(node == null) {
            for (String w: dict.keySet()) {
                double dist = getDistance(w, s);
                TreeMap<Integer, TreeSet<String>> similarWords = map.getOrDefault(dist, new TreeMap<>());
                int freq = dict.get(w);
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

    public double getDistance(String str1, String str2){
        return switch (this.type){
            case EDIT -> MinEdit.calculate(str1, str2);
            case HAMMING -> Hamming.calculate(str1, str2);
            case JARO -> Jaro.calculate(str1, str2);
            case QWERTY -> Qwerty.calculate(str1, str2);
            case COSINE -> Cosine.calculate(str1, str2);
        };
    }

}
