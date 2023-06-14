package bots.CFG;

import java.util.ArrayList;

public class MultiStageBot {
    public static ArrayList<Node> History = new ArrayList<Node>();
    public static int maxHistory=10;
    
    public static String getResponse(String input){
        input=CFG.cleanUpInput(input);
        Node tree= new Node("top",input);
        CFG.applyRules(tree, input);
        //add the history to the front of the array
        History.add(0,tree);
        //remove the last element if the array is too big
        if(History.size()>maxHistory){
            History.remove(History.size()-1);
        }

        Node[] trees= new Node[History.size()];
        
        trees=History.toArray(trees);

        int action= CFG.isAction(trees);
        return action!=-1 ? CFG.executeAction(tree, action) : "I don't know how to answer that";
    }

    public static void ClearHistory(){
        History.clear();
    }

    public static void setHistory(ArrayList<Node> history){
        History=history;
    }

    public static ArrayList<Node> getHistory(){
        return History;
    }
    
    public static void main(String[] args) {
        String first="I want something to eat";
        String second="hotdog";
        String answer1=getResponse(first);
        System.out.println(answer1);
        String answer2=getResponse(second);
        System.out.println(answer2);
    }
}
