package TemplateSkills;

import gui.ChatApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class TemplateSkills {
    //class that does the same thing as the CFG.java
    //but for a simpler template
    public static String[] templates=readTemplates();


    public static void main(String[] args) {
        String input="Which lectures are there on Saturday at 9?";
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
            //if they have unequal length, they can't match
            if(TemplateWords.length!=inputWords.length){
                continue;
            }
            
            boolean match=true;
            //check if the input matches the template and call checkKeyWord if a word starts with <>
            ArrayList<String> values = new ArrayList<String>();
            for(int j=0; j<TemplateWords.length; j++){
                if(TemplateWords[j].startsWith("<")&&TemplateWords[j].endsWith(">")){
                    if(checkKeyWord(TemplateWords[j], inputWords[j], i)){
                        values.add(inputWords[j]);
                    }
                    else{
                        match=false;
                        break;
                    }
                }
                else if(!TemplateWords[j].equals(inputWords[j])){
                    match=false;
                    break;
                }
            }
            if(!match){
                continue;
            }
            for(int j=i+1; j<templates.length; j++){
                parts = templates[j].split(" ");
                if(parts[0].equals("action")){
                    //the string is formated as Action <DAY> Monday <TIME> 9 We start the week with math
                    //we need to check if the values match the template
                    //and return the rest of the string
                    String[] parts2 = templates[j].split(" ");
                    int k=1;
                    int l=0;
                    boolean matching = true;
                    while(parts[k].contains("<")){
                        if(!parts[k+1].equals(values.get(l))){
                            matching=false;
                            break;
                        }
                        l++;
                        k+=2;
                    }
                    if(matching){
                        String output="";
                        for(int m=k; m<parts.length; m++){
                            output+=parts[m]+" ";
                        }
                        return output;
                    }
                }	
            }
            
        }
        return "I'm sorry, I don't understand that.";
    }

    public static boolean checkKeyWord(String Keyword,String value,  int line){
        //remove <> from keyword
        //Keyword=Keyword.substring(1, Keyword.length()-1);
        for(int i=line+1;i<templates.length;i++){
            //loop until you find a line that starts with "Question"
            String current=templates[i];
            if(templates[i].startsWith("question")){
                break;
            }
            String[] parts = templates[i].split(" ");
            if(parts[0].equals("slot")&&parts[1].equals(Keyword)){
                if(value.equals(parts[2])){
                    return true;
                }
            }

        }
        return false;
    }


    public static String[] readTemplates(){
        //read in the templates from a file called templateskills.txt
        //and store them in the templates array
        String[] templates = new String[0];
        try{
            Scanner scanner = new Scanner(new File( System.getProperty("user.dir") + "/src/main/resources/txts/CFGTemplate.txt"));
            ArrayList<String> lines = new ArrayList<String>();
            while(scanner.hasNextLine()){
                lines.add(scanner.nextLine().toLowerCase());
            }
            templates = lines.toArray(new String[lines.size()]);
        }
        catch(Exception e){
            System.out.println("Error reading templateskills.txt");
        }
        return templates;

    }
    
}