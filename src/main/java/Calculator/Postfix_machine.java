package Calculator;

import java.util.*;
import java.util.regex.Pattern;

public class Postfix_machine {

    public String infixToPostfix(String infixExpression){

        Stack<String> operatorStack = new Stack<>();
        ArrayList<String> postfixList = new ArrayList<>();

        infixExpression = addSpaces(infixExpression);
        String[] tokens = infixExpression.split(" ");

        for(String token : tokens){
            if(token.length() == 0){
                continue;
            }
            if(isOperator(token)){
                while(true){
                    if(operatorStack.isEmpty() || "(".equals(operatorStack.peek()) || getPriority(token) > getPriority(operatorStack.peek())){
                        operatorStack.push(token);
                        break;
                    }
                    postfixList.add(operatorStack.pop());
                }
            }else if (isNumber(token)) {
                postfixList.add(token);
            } else if(token.equals(".")){
                postfixList.add(token);
            }else if ("(".equals(token)) {
                operatorStack.push(token);
            }else if (")".equals(token)){
                while (true){
                    if ("(".equals(operatorStack.peek())){
                        operatorStack.pop();
                        break;
                    }else{
                        postfixList.add(operatorStack.pop());
                    }
                }
            }else {
                throw new IllegalArgumentException("Failed to identify " + token);
            }
        }while (!operatorStack.isEmpty()){
            postfixList.add(operatorStack.pop());
        }
        StringBuilder sb = new StringBuilder();
        for (String s : postfixList) {
            sb.append(s).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private int getPriority(String operator){
        if("*".equals(operator) || "/".equals(operator)){
            return 2;
        }else if("+".equals(operator) || "-".equals(operator)){
            return 1;
        }else{
            return 0;
        }
    }

    private boolean isOperator(String token){
        return "+".equals(token) || "-".equals(token) || "*".equals(token) || "/".equals(token);
    }

    private boolean isNumber(String token){
        Pattern pattern = Pattern.compile("[0-9]*");
        if(token.indexOf(".")>0){
            if(token.indexOf(".")==token.lastIndexOf(".") && token.split("\\.").length==2){
                return pattern.matcher(token.replace(".","")).matches();
            }else {
                return false;
            }
        }else {
            return pattern.matcher(token).matches();
        }
    }


    private String addSpaces(String expression){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < expression.length(); i++){
            char c = expression.charAt(i);
            if(c == '(' || c == ')' || c == '+' || c == '-' || c == '*' || c == '/'){
                sb.append(" ");
                sb.append(c);
                sb.append(" ");
            }else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public String getResult(String equation) {
        String[] arr = equation.split(",");
        List<String> list = new ArrayList<String>();

        for (String s : arr) {
            int size = list.size();
            switch (s) {
                case "+" -> {
                    double a = Double.parseDouble(list.remove(size - 2)) + Double.parseDouble(list.remove(size - 2));
                    list.add(String.valueOf(a));
                }
                case "-" -> {
                    double b = Double.parseDouble(list.remove(size - 2)) - Double.parseDouble(list.remove(size - 2));
                    list.add(String.valueOf(b));
                }
                case "*" -> {
                    double c = Double.parseDouble(list.remove(size - 2)) * Double.parseDouble(list.remove(size - 2));
                    list.add(String.valueOf(c));
                }
                case "/" -> {
                    double d = Double.parseDouble(list.remove(size - 2)) / Double.parseDouble(list.remove(size - 2));
                    list.add(String.valueOf(d));
                }
                default -> list.add(s);
            }
        }
        return list.size() == 1 ? list.get(0) : "Invalid Equation";

    }
}

