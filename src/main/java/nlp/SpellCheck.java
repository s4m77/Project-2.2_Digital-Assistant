package nlp;

import java.util.*;

public class SpellCheck {

    private static final Trie trie = new Trie();
    private static final Map<String, Integer> dict = new HashMap<>();

    public static void main(String[] args){
        System.out.println(minEditDistance("apple","p"));
        System.out.println(SpellCheck.findBestMatch("banane"));
    }

    public static String findBestMatch(String input){
        List<String> corpus = TextFileReader.read("src/main/java/nlp/Corpus.txt");
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
        TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> map = new TreeMap<>();
        TrieNode node = trie.find(s);
        if(node == null) {
            for (String w: dict.keySet()) {
                int dist = minEditDistance(w, s);
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

    public static int minEditDistance(String str1, String str2) {
        int l = str1.length();
        int m = str2.length();
        int[][] editDist = new int[l+1][m+1];
        for (int i = 0; i <= l; i++) {
            for (int j = 0; j <= m; j++) {
                if (i == 0)
                    editDist[i][j] = j;
                else if (j == 0)
                    editDist[i][j] = i;
                else if (str1.charAt(i-1) == str2.charAt(j-1))
                    editDist[i][j] = editDist[i-1][j-1];
                else if (i>1 && j>1  && str1.charAt(i-1) == str2.charAt(j-2)
                        && str1.charAt(i-2) == str2.charAt(j-1))
                    editDist[i][j] = 1+Math.min(Math.min(editDist[i-2][j-2], editDist[i-1][j]), Math.min(editDist[i][j-1], editDist[i-1][j-1]));
                else
                    editDist[i][j] = 1 + Math.min(editDist[i][j-1], Math.min(editDist[i-1][j], editDist[i-1][j-1]));
            }
        }
        return editDist[l][m];
    }

}
