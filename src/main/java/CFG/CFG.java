package CFG;

import java.io.FileReader;
import java.util.ArrayList;
import java.lang.reflect.*;



public class CFG {
    

    public static final String CFG_FILE = System.getProperty("user.dir") + "/src/main/resources/txts/CFGTemplate.txt";
    public static ArrayList<String> rules=readCFG(CFG_FILE);
    public static String actionPrefix = "Action";
    public static String rulePrefix = "Rule";
    public static String dividerChar = ":";
    public static String freeString="@";
    public static int maxDepth=9;
    public static String divideByChar ="&";

    public static void main(String[] args) {
        //TODO Auto-generated method stub
        System.out.println("Asking a question");
        String sentence = "what is (200+2)+(2^20)";
        System.out.println(interpret("What lectures are there on monday?"));



        // String[] test={"1","0","*","2","+","3","*","4","0","*","5","*","6"};
        // String rule="Rule <MATHEXPRESSION&> <NUMBER> <OPERATOR> <MATHEXPRESSION> | <NUMBER> <OPERATOR> <NUMBER>";
        // Node tree = new Node("top",null);
        // applyRuleChar(tree, rule, test);
        // int tes2=-3;
        // TemplateMethods.Math(tree);
        // System.out.println(tree.getNode("<ANSWER>").value);

        // String[] characters={"2","0","0","*","2","0","0","*","2","0","0"};
        // String rule="Rule <MATHEXPRESSION&> <NUMBER> <OPERATOR> <MATHEXPRESSION> | <NUMBER> <OPERATOR> <NUMBER>";
        // Node tree = new Node("top",null);
        // applyRuleChar(tree, rule, characters);
        // TemplateMethods.Math(tree);
        // System.out.println(tree.getNode("<ANSWER>").value);

        // String rule = "<MATHEXPRESSION> <OPERATOR> <NUMBER>";
        // System.out.println(findSmallestAmountOfChars(rule));

    }


    
    public static String interpret(String sentence){
        //clean up the input
        sentence=cleanUpInput(sentence);
        //construct the tree based on the rules in the CFG
        Node tree = new Node("top",sentence);
        applyRules(tree, sentence);

        //check if the tree meets the requirements of an action
        int action= isAction(tree);
        //if it does, execute the action
//        if(action!=-1){
//            executeAction(tree, action);
//        }
        return action!=-1 ? executeAction(tree, action) : "I don't know how to answer that";
    }

    public static String cleanUpInput(String input){
        //set everything to lower case
        input=input.toLowerCase();
        //remove all . ? ! and ,
        //input=input.replace(".", "");
        input=input.replace("?", "");
        input=input.replace("!", "");
        //input=input.replace(",", "");
        //remove any leading or trailing spaces
        input=input.trim();
        return input;
    }

    public static String executeAction(Node tree, int action){
        ArrayList<String> rules2=rules;
        String[] split=rules.get(action).split(dividerChar);
        if(split.length==3){
            //if there is a third part, it will contain methods to be executed
            String methodString=split[2];
            //remove front and back spaces
            methodString=methodString.trim();
            //split the string into methods
            String[] methods=methodString.split(" ");




            
            //first replace every substring surrounded by < and > with the value of the attribute
            //for example Math(<PRO>) will become Math(he)
            for(int i=0;i<methods.length;i++){
                //every method will take as input the tree
                //execute the method for example Math()
                try{
                    Class<TemplateMethods> c=TemplateMethods.class;
                    Method method=c.getMethod(methods[i], Node.class);
                    method.invoke(null, tree);
                }
                catch(Exception e){
                    System.out.println("Error executing method "+methods[i]);
                }
            }
        }
        String actionString = "";
        String temp=split[1];
        //remove front and back spaces
        temp=temp.trim();
        String[] words=temp.split(" ");
        for(int j=0;j<words.length;j++){
            if(containsKey(words[j])){
                actionString+=tree.findValue(words[j])+" ";
            }
            else{
                actionString+=words[j]+" ";
            }
        }
        System.out.println(actionString);
        return actionString;
    }


    public static void applyRules(Node tree, String sentence){
        //for every rule, check if the sentence matches the rule
        String headRule=rules.get(0);
        String[] rule=sentence.split(" ");
        applyRule(tree, headRule, rule, 0);

    }
    public static boolean containsKey(String sentense){
        return sentense.contains("<") && sentense.contains(">");
    }

    
    public static int isAction(Node tree){
        // ArrayList<String> attributes =new ArrayList<>();
        // ArrayList<String> values = new ArrayList<>();
        int temp=0;
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
    // public static Boolean applyRule(Node tree,String Rule, String sentence){
    //     return applyRule(tree, Rule, sentence, false);
    // }

    public static Boolean applyRule(Node tree,String Rule, String[] sentence,int depth){
        if(depth>maxDepth){
            return false;
        }
        //start with rule:// Rule <PRO> I | you | he | she 
        //remove the first rule and attribute
        //then split the rule into parts
        String[] ruleParts = StripRule(Rule);
        String[] sentenceParts = sentence;//sentence.split(" ");
        
        //get attribute and remove any & 
        String attribute = Rule.split(" ")[1].replace(divideByChar, "");
        //list storing the indexes used in the sentence

        ArrayList<Integer> usedIndexes = new ArrayList<Integer>();
        
        
        for(int i2=0;i2<ruleParts.length;i2++){
            String[] words=ruleParts[i2].split(" ");
            Boolean wordFound=true;
            //check if the words appear in order in the sentence
            usedIndexes.clear();
            int index=0;
            int keywords=0;
            for(int j=index;j<words.length;j++){
                if(containsKey(words[j])){
                    keywords++;
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
            //if there are more keywords than words in the sentence
            //we can't use this rule
            if(keywords>sentenceParts.length){
                continue;
            }
            int foundIndex = i2;
                    
            ArrayList<String> nonUsedSentenceParts;
            
            nonUsedSentenceParts = getNonUsedSentenceParts(sentenceParts, usedIndexes);
            

            
            //we make a child node
            //if the rule part is * we want to add the whole sentence
            String value =ruleParts[foundIndex];
            if(ruleParts[foundIndex].equals(freeString)){
                value = "";
                for(int i=0;i<nonUsedSentenceParts.size();i++){
                    value+=nonUsedSentenceParts.get(i)+" ";
                }
                value = value.substring(0, value.length()-1);
            }
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
                        if(rule[0].equals("Rule") && (rule[1].equals("<"+subAttribute+">")||rule[1].equals("<"+subAttribute+ divideByChar +">"))){
                        
                            Boolean temp=rule[1].equals("<"+subAttribute+ divideByChar +">");
                            if(temp){
                                //if the contains a devide by char we want to split the whole sentence into its individual parts
                                String[] characters=subSentence.split("");
                                if(applyRuleChar(child, rules.get(j), characters,depth+1)!=-1){
                                    ruleFound = true;
                                    break;
                                }
                            }
                            else{
                                String[] subSentenceParts=subSentence.split(" ");
                                if(applyRule(child, rules.get(j), subSentenceParts,depth+1)){
                                    ruleFound = true;
                                    break;
                                }
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

    public static int applyRuleChar(Node tree,String Rule,String[] characters){
        return applyRuleChar(tree, Rule, characters, 0);
    }
    private static int applyRuleChar(Node tree,String Rule, String[] characters ,int depth){
        //does the same as applyRule but on a character by character basis
        if(depth>maxDepth){
            return -1;
        }
        //start with rule:// Rule <MATHEXPRESSION&> <MATHEXPRESSION> <OPERATOR> <NUMBER> | <NUMBER> <OPERATOR> <NUMBER>
        //remove the first rule and attribute
        //then split the rule into parts
        String[] ruleParts = StripRule(Rule);
       
        //get attribute and remove any &
        String attribute = Rule.split(" ")[1].replace(divideByChar, "");
        
        for(int i2=0;i2<ruleParts.length;i2++){
            Node child = new Node(attribute,ruleParts[i2]);
            String[] ruleChars = ruleParts[i2].split(" "); 
            if(findSmallestAmountOfChars(ruleParts[i2])>characters.length){
                continue;
            }
            int usedChars=0;
            Boolean ruleUsed=true;
            for(int i=0;i<ruleChars.length;i++){

                if(containsKey(ruleChars[i])){
                    //find a rule that matches the word
                    String subAttribute=ruleChars[i].substring(1, ruleChars[i].length()-1);
                    Boolean ruleFound = false;
                    for(int j=0;j<rules.size();j++){
                        String[] rule = rules.get(j).split(" ");
                        if(rule[0].equals("Rule") && (rule[1].equals("<"+subAttribute+">")||rule[1].equals("<"+subAttribute+ divideByChar +">"))){
                            String[] subSentenceParts= new String[characters.length-usedChars];
                            for(int k=0;k<subSentenceParts.length;k++){
                                subSentenceParts[k]=characters[usedChars+k];
                            }
                            int temp=applyRuleChar(child, rules.get(j), subSentenceParts,depth+1);
                            if(temp!=-1){
                                usedChars+=temp;
                                ruleFound = true;
                                break;
                            }
                        }
                    }
                    if(!ruleFound){
                        ruleUsed=false;
                        break;
                    }
                }
                else{
                    if(!ruleChars[i].equals(characters[usedChars])){
                        ruleUsed=false;
                        break;
                    }
                    usedChars++;
                }
            }
            if(ruleUsed){
            
                
                tree.addChild(child);
                return usedChars;
            }

        }
        return -1;

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
    public static ArrayList<String> getNonUsedCharacters(String[] sentenceParts, ArrayList<Integer> usedIndexes){
        ArrayList<String> nonUsedSentenceParts= new ArrayList<String>();
        for(int i=0;i<sentenceParts.length;i++){
            if(!usedIndexes.contains(i)){
                nonUsedSentenceParts.add(sentenceParts[i]);
            }
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




    public static int findSmallestAmountOfChars(String  Rule){
        return findSmallestAmountOfCharsTop(Rule);
    }


    public static int findSmallestAmountOfCharsSub(String  Rule,int depth){
        if(depth>1){
            return 99999999;
        }
        int smallestAmountOfChars=99999999;
        //String attribute=Rule.substring(Rule.indexOf("<")+1,Rule.indexOf(">")).replace(devideByChar,"");
        String[] subRules=StripRule(Rule);
        for(int i=0;i<subRules.length;i++){
            String current=subRules[i];
            String[] subRuleParts=subRules[i].split(" ");
            int amountOfChars=0;
            for(int j=0;j<subRuleParts.length;j++){
                if(subRuleParts[j].contains("<")){
                    for(int k=0;k<rules.size();k++){
                        String[] rule = rules.get(k).split(" ");
                        if(rule[0].equals("Rule") && (rule[1].equals(subRuleParts[j])||rule[1].replace(divideByChar,"").equals(subRuleParts[j]))){
                            int smallest=findSmallestAmountOfCharsSub(rules.get(k),depth+1);
                            amountOfChars+=smallest;
                            break;
                        }
                    }
                }else{
                    amountOfChars++;
                }
            }
            if(amountOfChars<smallestAmountOfChars){
                smallestAmountOfChars=amountOfChars;
            }
            
        }
    
        return smallestAmountOfChars;
    }


    public static int findSmallestAmountOfCharsTop(String  Rule){
        int smallestAmountOfChars=99999999;
        //String attribute=Rule.substring(Rule.indexOf("<")+1,Rule.indexOf(">")).replace(devideByChar,"");
       
        String[] subRuleParts=Rule.split(" ");
        int amountOfChars=0;
        for(int j=0;j<subRuleParts.length;j++){
            if(subRuleParts[j].contains("<")){
                for(int k=0;k<rules.size();k++){
                    String[] rule = rules.get(k).split(" ");
                    if(rule[0].equals("Rule") && (rule[1].equals(subRuleParts[j])||rule[1].replace(divideByChar,"").equals(subRuleParts[j]))){
                        int smallest=findSmallestAmountOfCharsSub(rules.get(k),0);
                        amountOfChars+=smallest;
                        break;
                    }
                }
            }else{
                amountOfChars++;
            }
        }
        if(amountOfChars<smallestAmountOfChars){
            smallestAmountOfChars=amountOfChars;
        }
        
        
    
        return smallestAmountOfChars;
    }



}