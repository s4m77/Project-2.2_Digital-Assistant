package CYK;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RuleEngine {

    public static final ArrayList<Rule> rules = new ArrayList<>();
    private static final String PATH = "src/main/java/CYK/CFGTemplateNew.txt";
    private static final List<List<String>> allPhrases = new ArrayList<>();
    private static Rule actionRule = null;
    private static int longestPhraseSize = 0;

    public static List<Rule> getRules() {
        return rules;
    }

    public static List<Rule> getRulesCopy() {
        List<Rule> copy = new ArrayList<>();
        rules.forEach(r -> copy.add(r.copy()));
        return copy;
    }

    public static boolean loadRules(){
        rules.clear();

        int id = 0;
        List<String> textFromFile = read(PATH);
        for(String line : textFromFile){
            Rule rule = new Rule();
            rule.id=id++;
            String[] symbols = line.split(" ");
            //checks if valid start of production rule
            if(isVariable(symbols[0])){
                rule.variable=symbols[0];
            } else {
                return false;
            }
            StringBuilder expression = new StringBuilder();
            List<String> splitExpression = new ArrayList<>();
            for (int i = 1; i < symbols.length; i++) {
                String symbol = symbols[i];

                if(symbol.equals("|")){
                    if(!expression.toString().equals("")){
                        rule.expressions.add(expression.toString());
                        rule.splitExpressions.add(splitExpression);
                    }
                    expression = new StringBuilder();
                    splitExpression = new ArrayList<>();
                } else if(symbol.equals("@")){
                    if(!expression.toString().equals("")){
                        expression.append(" ");
                    }
                    expression.append(symbol);
                    splitExpression.add(symbol);

                } else {
                    if (!expression.toString().equals("")) {
                        expression.append(" ");
                    }
                    expression.append(symbol);
                    splitExpression.add(symbol);
                }
            }

            if(!expression.toString().equals("")){
                rule.expressions.add(expression.toString());
                rule.splitExpressions.add(splitExpression);
            }
            if(rule.variable.equals("<ACTION>")){
                actionRule=rule;
            }
            rules.add(rule);
        }
        combos();
        return true;
    }

    public static boolean isVariable(String string){
        return string.length() > 2 && string.charAt(0) == '<' && string.charAt(string.length() - 1) == '>';
    }

    public static List<String> read(String path){
        List<String> text = new ArrayList<>();
        try {
            File textFile = new File(path);
            Scanner myReader = new Scanner(textFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                text.add(data);
            }
            myReader.close();
            System.out.println(path + ": read.");
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return text;
    }

    /**
     * Builds all possible phrases from CFG
     * starting point is the <ACTION> rule
     */
    public static void combos() {
        List<List<String>> unExpressed = new ArrayList<>();
        for(List<String> list : actionRule.splitExpressions){
            List<String> copy = new ArrayList<>(list);
            unExpressed.add(copy);
        }
        while(!unExpressed.isEmpty()){
            List<String> phrase = unExpressed.remove(0);
            boolean fullyExpressed = true;
            for (int i = 0; i < phrase.size(); i++) {
                String word = phrase.get(i);
                if(isVariable(word)){
                    Rule child = findRule(word);
                    for(List<String> expression : child.splitExpressions){
                        unExpressed.add(replace(phrase,expression,i));
                    }
                    fullyExpressed=false;
                }
            }
            if(fullyExpressed){
                boolean duplicate = false;
                for(List<String> list : allPhrases){
                    String target = String.join(" ", list);
                    String word = String.join(" ", phrase);
                    if(target.equals(word)){
                        duplicate=true;
                    }
                }
                if(!duplicate){
                    allPhrases.add(phrase);
                    int total = 0;
                    for(String str : phrase){
                        total += str.length();
                    }
                    longestPhraseSize = Math.max(total,longestPhraseSize);
                }

            }
        }
    }
    private static Rule findRule (String s){
        return rules.stream().filter(r-> r.variable.equals(s)).findFirst().orElse(null);
    }
    private static List<String> replace (List<String> phrase, List<String> expression, int position){
        List<String> str = new ArrayList<>(phrase);
        str.remove(position);
        for(String word : expression){
            if(position>=str.size()){
                str.add(word);
            } else {
                str.add(position,word);
            }
            position++;
        }
        return str;
    }
}
