package CFG;

import java.io.FileReader;
import java.util.ArrayList;



public class CFG {
    

    public static final String CFG_FILE = "src\\main\\java\\CFG\\CFGTemplate.txt";
    public static ArrayList<String> rules=readCFG(CFG_FILE);
    public static String actionPrefix = "Action";
    public static String rulePrefix = "Rule";
    public static String dividerChar = ":";
    public static String freeString="*";
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String sentence = "Which lectures are there on monday at 9";
        interpret(sentence);
    }


    
    public static void interpret(String sentence){
        //capitalisation doesn't matter
        sentence=sentence.toLowerCase();
        //construct the tree based on the rules in the CFG
        Node tree = new Node("top",sentence);
        applyRules(tree, sentence);

        //check if the tree meets the requirements of an action
        int action= isAction(tree);
        if(action!=-1){
            executeAction(tree, action);
        }

    }
    public static void executeAction(Node tree, int action){
        String[] split=rules.get(action).split(dividerChar);
        if(split.length==3){
            //if there is a third part, it will contain methods to be executed
            String[] methods = split[2].split(" ");
            //first replace every substring surrounded by < and > with the value of the attribute
            //for example Math(<PRO>) will become Math(he)
            for(int i=0;i<methods.length;i++){
                //every method will take as input the tree
                //execute the method for example Math()
                java.lang.reflect.Method method;
                try{
                    method=TemplateMethods.class.getMethod(methods[i], Node.class);
                    method.invoke(null, tree);
                }
                catch(Exception e){
                    System.out.println("Error executing method "+methods[i]);
                }
            }
        }
        String actionString = "";
        String[] words = split[1].split(" ");
        for(int j=0;j<words.length;j++){
            if(containsKey(words[j])){
                actionString+=tree.findValue(words[j])+" ";
            }
            else{
                actionString+=words[j]+" ";
            }
        }
        System.out.println(actionString);
    }


    public static void applyRules(Node tree, String sentence){
        //for every rule, check if the sentence matches the rule
        for(int i=0;i<rules.size();i++){
            String rule = rules.get(i);
            if(rule.contains(rulePrefix)){
                if(applyRule(tree, rule, sentence)){
                    break;
                } 
            }
        }
    }
    public static boolean containsKey(String sentense){
        return sentense.contains("<") && sentense.contains(">");
    }

    
    public static int isAction(Node tree){
        // ArrayList<String> attributes =new ArrayList<>();
        // ArrayList<String> values = new ArrayList<>();
        for(int i=0;i<rules.size();i++){
            // attributes.clear();
            // values.clear();
            boolean action = true;
            String[] rule = rules.get(i).split(dividerChar)[0 ].split(" ");
            if(rule[0].equals(actionPrefix)){
                for(int j=0;j<rule.length;j++){
                    if(containsKey(rule[j])){
                        String attribute = rule[j];
                        String value = tree.findValue(attribute);
                        if(value!=null&&(value.equals(rule[j+1])||rule[j+1].equals(freeString))){
                            // attributes.add(attribute);
                            // values.add(value);
                        }
                        else{
                            action=false;
                            break;
                        }
                    }
                    if(!action){
                        break;
                    }
                }
                if(action){
                    return i;
                }
                if(action){
                    String actionString = "";
                    String[] words = rules.get(i).split(dividerChar)[1].split(" ");
                    for(int j=0;j<words.length;j++){
                        if(containsKey(words[j])){
                            actionString+=tree.findValue(words[j])+" ";
                        }
                        else{
                            actionString+=words[j]+" ";
                        }
                    }
                    System.out.println(actionString);
                    break;
                }
            }
        }
        return -1;
    }

    public static Boolean applyRule(Node tree,String Rule, String sentence){
        //start with rule:// Rule <PRO> I | you | he | she 
        //remove the first rule and attribute
        //then split the rule into parts
        String[] ruleParts = StripRule(Rule);
        String[] sentenceParts = sentence.split(" ");
        String attribute = Rule.split(" ")[1];
        //list storing the indexes used in the sentence
        ArrayList<Integer> usedIndexes = new ArrayList<Integer>();
        int foundIndex = 0;
        for(int i2=0;i2<ruleParts.length;i2++){
            String[] words=ruleParts[i2].split(" ");
            Boolean wordFound=true;
            //check if the words appear in order in the sentence
            usedIndexes.clear();
            int index=0;
            for(int j=index;j<words.length;j++){
                if(containsKey(words[j])){
                    continue;
                }
                Boolean found = false;
                for(int k=index;k<sentenceParts.length;k++){
                    if(sentenceParts[k].equals(words[j])||words[j].equals(freeString)){
                        found = true;
                        usedIndexes.add(k);
                        index=k+1;
                        break;
                    }
                }
                if(!found){
                    wordFound=false;
                    break;
                }
            }
            if(!wordFound){
                continue;
            }
                    
        
        

            ArrayList<String> nonUsedSentenceParts = getNonUsedSentenceParts(sentenceParts, usedIndexes);
            //we make a child node
            //if the rule part is * we want to add the whole sentence
            String value = ruleParts[foundIndex].equals(freeString)?sentence:ruleParts[foundIndex];
            Node child = new Node(attribute,value);
            Boolean subCorrect = true;
            //for every word in the rule containing < > 
            //recall this function
            int amountofwords=ruleParts[foundIndex].split(" ").length;
            for(int i=0;i<amountofwords && nonUsedSentenceParts.size()>0;i++){
                if(ruleParts[foundIndex].split(" ")[i].contains("<") && ruleParts[foundIndex].split(" ")[i].contains(">")){
                    String subAttribute = ruleParts[foundIndex].split(" ")[i].substring(1, ruleParts[foundIndex].split(" ")[i].length()-1);
                    String subSentence = nonUsedSentenceParts.remove(0);
                    //find a rule that matches the word
                    Boolean ruleFound = false;
                    for(int j=0;j<rules.size();j++){
                        String[] rule = rules.get(j).split(" ");
                        if(j==7){
                            int test=0;
                        }
                        if(rule[0].equals("Rule") && rule[1].equals("<"+subAttribute+">")){
                            if(applyRule(child, rules.get(j), subSentence)){
                                ruleFound = true;
                                break;
                            }
                        }
                        if(ruleFound){
                            break;
                        }
                    }
                    if(!ruleFound){
                        subCorrect = false;
                        break;
                    }
                }
            }
            if(!subCorrect){
                continue;
            }
            //if the rule is correct we add the child to the tree
            tree.addChild(child);
            return true;

        }
        return false;

        
    }


    public static ArrayList<String> getNonUsedSentenceParts(String[] sentenceParts, ArrayList<Integer> usedIndexes){
        //method which returns the parts of the sentence that are not used in the rule
        //for example if the sentence is "I like to eat apples" and the rule is "I like <food>"
        //the method will return "to eat apples"
        ArrayList<String> nonUsedSentenceParts= new ArrayList<String>();
        for(int i=0;i<usedIndexes.size()-1;i++){
            if(usedIndexes.get(i+1)-usedIndexes.get(i)>1){
                String nonUsedSentencePart = "";
                for(int j=usedIndexes.get(i)+1;j<usedIndexes.get(i+1);j++){
                    nonUsedSentencePart+=sentenceParts[j]+" ";
                }
                nonUsedSentencePart = nonUsedSentencePart.substring(0, nonUsedSentencePart.length()-1);
                nonUsedSentenceParts.add(nonUsedSentencePart);
            }
        }

        if(usedIndexes.size()==0){
            String nonUsedSentencePart = "";
            for(int j=0;j<sentenceParts.length;j++){
                nonUsedSentencePart+=sentenceParts[j]+" ";
            }
            nonUsedSentencePart = nonUsedSentencePart.substring(0, nonUsedSentencePart.length()-1);
            nonUsedSentenceParts.add(nonUsedSentencePart);
        }

        //if the last index is not the last word in the sentence, add the words after the last index to the list
        
        if(usedIndexes.size()>0 &&usedIndexes.get(usedIndexes.size()-1)!=sentenceParts.length-1){
            String nonUsedSentencePart = "";
            for(int j=usedIndexes.get(usedIndexes.size()-1)+1;j<sentenceParts.length;j++){
                nonUsedSentencePart+=sentenceParts[j]+" ";
            }
            nonUsedSentencePart = nonUsedSentencePart.substring(0, nonUsedSentencePart.length()-1);
            nonUsedSentenceParts.add(nonUsedSentencePart);
        }

        return nonUsedSentenceParts;
    }




    public static String[] StripRule(String rule){
        // strip the rule into a string array
        // "Rule <SCHEDULE> Which lectures are there <TIMEEXPRESSION> | <TIMEEXPRESSION> which lectures are there"
        // becomes ["Which lectures are there <TIMEEXPRESSION>","<TIMEEXPRESSION> which lectures are there""]
        String temp=rule.substring(rule.indexOf(">")+2);
        String[] result = temp.split(" \\| ");
        
        return result;
    }

    public static ArrayList<String> readCFG(String file){
        try{
            // read the file line by line and put the results in an arraylist
            ArrayList<String> rules = new ArrayList<String>();
            FileReader fr = new FileReader(file);
            int i;
            String line = "";
            while((i=fr.read())!=-1){
                if((char)i == '\n'){
                    rules.add(line);
                    line = "";
                }
                else if(i==13){
                    //do nothing (this is a carriage return)
                }else{
                    line += (char)i;
                }
            }
            fr.close();
            return rules;

        } catch (Exception e) {
            System.out.println("Error reading file: " + file);
            throw new RuntimeException(e);
        }

    }


}