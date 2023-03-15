package Calculator;

import java.util.ArrayList;
import java.util.Scanner;

public class CalcAssist {
    public static ArrayList<String> memoryList = new ArrayList<>(); // storage list

    // get current result
    public static String getCurrentResult(String str){
        Postfix_machine cc = new Postfix_machine();
        String a = cc.infixToPostfix(str);
        return cc.getResult(a);
    }
    // set recent memory value
    public static void setMemoryValue(String memval){
        memoryList.add(memval);
    }
    // clear the list
    public static void clearMemory(){
        memoryList.clear();
    }
    // get all stored results
    public static void getMemoryValue(){
        for (Object o : memoryList) {
            System.out.println(o);
        }
    }
    // get a certain stored result
    public static String getHistoryValue(int index){
        if (index < memoryList.size())
            return memoryList.get(index);
        return ("Array out of bounds, input error");
    }
    // main function
    public static void main(String[] args) {

        while (true) {
            System.out.println();
            System.out.println("Enter new expression:");
            Scanner input = new Scanner(System.in);
            String expression = input.nextLine(); // input expression with spaces
            String result = CalcAssist.getCurrentResult(expression);
            System.out.println("Result is: " + result);
            System.out.println("Next operation (m-store, c-clear, mr-allHistory, h-searchHistory, o-endTask):");
            Scanner next = new Scanner(System.in);
            String key = next.next();
            String current = "";
            switch (key) {
                case "m" -> {
                    CalcAssist.setMemoryValue(expression + " = " + result); // store and choose to do second operation
                    System.out.println("Continue on this result (i-yes, n-cancel):");
                    String n = input.next();
                    switch (n) {
                        case "n" -> {
                            continue;
                        } // do not operate
                        case "i" -> { // operate and input expression
                            System.out.println("Last expression result is " + result);
                            System.out.println("Enter expression (operator+operand):");
                            Scanner input1 = new Scanner(System.in);
                            String expression1 = input1.nextLine();
                            current = CalcAssist.getCurrentResult(result + expression1);
                            CalcAssist.setMemoryValue(result + expression1 + " = " + current); // store and choose to do second operation
                            System.out.println("Result is " + current);
                        }
                    }
                }
                case "c" -> {
                    CalcAssist.clearMemory();
                } // clear
                case "mr" -> {
                    CalcAssist.getMemoryValue();
                }
                case "h" -> {
                    System.out.println("Select the id you want to search:");
                    int id = next.nextInt(); // input id to search
                    System.out.println(CalcAssist.getHistoryValue(id));
                }
                case "o" -> {
                    return;
                }
                default ->
                        System.out.println("Invalid input.");
            }
        }
    }

}
