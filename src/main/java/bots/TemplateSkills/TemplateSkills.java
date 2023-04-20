package bots.TemplateSkills;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class TemplateSkills {
    //class that does the same thing as the bots.CFG.java
    //but for a simpler template
    public static String[] templates=readTemplates();


    public static void main(String[] args) {
        String input="How do i get from maastricht to heerlen at 9 am";
        String test=interpret(input);
        System.out.println(test);
    }
    public static String interpret(String input){
        //remove punctuation, Uppercases and ?, !, . from the input
        input=input.toLowerCase();
        input=input.replace("?", "");
        input=input.replace("!", "");
        input=input.replace(".", "");
        String[] inputWords = input.split(" ");

        //make a stack of the words in the input with the first word on top
        //and the last word at the bottom

        for(int i=0; i<templates.length; i++){
            String[] parts = templates[i].split(" ");
            if(!parts[0].equals("question")){
                continue;
            }
            //remove first word
            String[] TemplateWords = new String[parts.length-1];
            for(int j=1; j<parts.length; j++){
                TemplateWords[j-1]=parts[j];
            }
            
            
            boolean match=true;
            //check if the input matches the template and call checkKeyWord if a word starts with <>
            ArrayList<String> values = new ArrayList<String>();
            int keyWordIndex=0;
            for(int j=0; j<TemplateWords.length; j++){
                if(TemplateWords[j].contains("<")){
                    //check if the word is a keyword
                    String restofwords=getRestOfString(inputWords, keyWordIndex);
                    int amountofwords =checkKeyWord(restofwords, TemplateWords[j], i);
                    if(amountofwords==-1){
                        match=false;
                        break;
                    }
                    else{
                        String value="";
                        for(int k=0; k<amountofwords; k++){
                            value+=inputWords[j+k]+" ";
                        }
                        //remove the last space
                        value=value.substring(0, value.length()-1);
                        values.add(value);
                        keyWordIndex+=amountofwords;
                    }
                }
                else{
                    //check if the word is the same as the input
                    if(!TemplateWords[j].equals(inputWords[keyWordIndex])){
                        match=false;
                        break;
                    }
                    keyWordIndex++;
                }
            }
            if(!match){
                continue;
            }
            for(int j=i+1; j<templates.length; j++){
                String[] split1=templates[j].split(":");
                String[] parts1=split1[0].split(" ");
                


                if(parts1[0].equals("action")){
                    String parts2=split1[1];
                    int valuesindex=0;
                    boolean match2=true;
                    //Action <name> bob dylan <age> 23
                    for(int k=1; k<parts1.length; k++){
                        if(parts1[k].contains("<")){
                            String condition=getStringFromFirstPart(parts1, k);
                            String value=values.get(valuesindex);
                            if(!value.equals(condition)){
                                match2=false;
                                break;
                            }
                            valuesindex++;
                        }
                    }
                    if(match2){
                        return parts2;
                    }
                }	
            }
            
        }
        return "I'm sorry, I don't understand that.";
    }

    public static int checkKeyWord(String RestOfSentence,String value,  int line){
        //Keyword=Keyword.substring(1, Keyword.length()-1);
        for(int i=line+1;i<templates.length;i++){
            //loop until you find a line that starts with "Question"
            String current=templates[i];
            if(templates[i].startsWith("question")){
                break;
            }
            String[] parts = templates[i].split(" ");
            if(parts[0].equals("slot")&&parts[1].equals(value)){
                Boolean match=true;
                for(int j=2, k=0; j<parts.length; k++,j++){
                    if(!parts[j].equals(RestOfSentence.split(" ")[k])){
                        match=false;
                        break;
                    }
                }
                if(match){
                    return parts.length-2;
                }
            }

        }
        return -1;
    }


    public static String[] readTemplates(){
        //read in the templates from a file called template-skills.txt
        //and store them in the templates array
        String[] templates = new String[0];
        try{
            Scanner scanner = new Scanner(new File( System.getProperty("user.dir") + "/src/main/resources/texts/template-skills.txt"));
            ArrayList<String> lines = new ArrayList<String>();
            while(scanner.hasNextLine()){
                lines.add(scanner.nextLine().toLowerCase());
            }
            templates = lines.toArray(new String[lines.size()]);
        }
        catch(Exception e){
            System.out.println("Error reading template-skills.txt");
        }
        return templates;

    }


    public static String getRestOfString(String[] words, int index){
        String output="";
        for(int i=index; i<words.length; i++){
            output+=words[i]+" ";
        }
        return output;
    }

    public static String getStringFromFirstPart(String[] words, int index){
        //string on index containss <> return all words after it until the next <>
        String output="";
        for(int i=index+1; i<words.length; i++){
            if(words[i].contains("<")){
                break;
            }
            output+=words[i]+" ";
        }
        //remove the last space
        output=output.substring(0, output.length()-1);
        return output;
    }
    
}