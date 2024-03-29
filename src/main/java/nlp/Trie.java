package nlp;

public class Trie {
    private TrieNode root = new TrieNode();
    private int myHashCode = 0;

    public void add(String word) {
        TrieNode p = root;
        for (char c : word.toCharArray()) {
            if(Character.isLetter(c)){
                if (p.nodes[c-'a'] == null)
                    p.nodes[c-'a'] = new TrieNode();
                p = p.nodes[c-'a'];
            }
        }
        myHashCode +=word.hashCode();
        p.incrementValue();
        p.isEnd = true;
    }

    public TrieNode find(String word) {
        TrieNode p = root;
        for (char c : word.toCharArray()) {
            if(Character.isLetter(c)){
                if (p.nodes[c-'a'] == null)
                    return null;
                p = p.nodes[c-'a'];
            }
        }
        if( p != null && p.isEnd)
            return p;
        return null;
    }

    public int getNodeCount() {
        return countNodesInTrie(root);
    }

    private int countNodesInTrie(TrieNode node)  {
        if (node == null)
            return 0;
        int count = 0;
        for (int i = 0; i < 26; i++) {
            if (node.nodes[i] != null)
                count += countNodesInTrie(node.nodes[i]);
        }
        return (1 + count);
    }

    public int getWordCount() {
        return wordCount(root);
    }

    private int wordCount(TrieNode root)  {
        int result = 0;
        if (root.isEnd)
            result++;
        for (int i = 0; i < 26; i++)
            if (root.nodes[i] != null )
                result += wordCount(root.nodes[i]);
        return result;
    }

    public String toString() {
        char[] wordArray = new char[50];
        StringBuilder sb = new StringBuilder();
        printAllWords(root, wordArray, 0, sb);
        if(sb.toString().length()==0)
            return "";
        return sb.substring(1);
    }

    private void printAllWords(TrieNode root, char[] wordArray, int pos, StringBuilder sb) {
        if(root == null)
            return;
        if(root.isEnd)  {
            sb.append("\n");
            for(int i=0; i<pos; i++) {
                sb.append(wordArray[i]);
            }
        }
        for(int i=0; i<26; i++) {
            if(root.nodes[i] != null)  {
                wordArray[pos] = (char)(i+'a');
                printAllWords(root.nodes[i], wordArray, pos+1, sb);
            }
        }
    }

    @Override
    public int hashCode(){
        return myHashCode;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Trie) {
            Trie s = (Trie) o;
            if(this.getNodeCount() != s.getNodeCount())
                return false;
            if(this.getWordCount() != s.getWordCount())
                return false;
            return compareTrie(this, s);
        } else {
            return false;
        }
    }

    private boolean compareTrie(Trie p, Trie q) {
        String s1 = p.toString();
        String s2 = q.toString();
        if(s1.equals("") && s2.equals(""))
            return true;
        String[] strs1 = s1.split("\n");
        String[] strs2= s2.split("\n");
        if(strs1.length!= strs2.length)
            return false;
        for(String s: strs1) {
            TrieNode node1 = p.find(s);
            TrieNode node2 = q.find(s);
            if(node1.getValue()!=node2.getValue())
                return false;
        }
        return true;
    }
}