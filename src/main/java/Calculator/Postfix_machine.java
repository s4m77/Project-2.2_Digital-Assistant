package Calculator;

import java.util.*;

public class Postfix_machine {

    public String infixToPostfix(String infixExpression){

        Stack<String> opStack = new Stack<>();
        ArrayList<String> suffixList = new ArrayList<>();

        infixExpression = insertBlanks(infixExpression);
        String[] tokens = infixExpression.split(" ");

        for(String token : tokens){
            if(token.length() == 0){
                continue;
            }
            if(isOperator(token)){
                while(true){
                    if(opStack.isEmpty() || "(".equals(opStack.peek()) || priority(token) > priority(opStack.peek())){
                        opStack.push(token);
                        break;
                    }
                    suffixList.add(opStack.pop());
                }
            }else if (isNumber(token)){
                suffixList.add(token);
            }else if ("(".equals(token)){
                opStack.push(token);
            }else if (")".equals(token)){
                while (true){
                    if ("(".equals(opStack.peek())){
                        opStack.pop();
                        break;
                    }else{
                        suffixList.add(opStack.pop());
                    }
                }
            }else {
                throw new IllegalArgumentException("wrong identify"+ token);
            }
        }while (!opStack.isEmpty()){
            suffixList.add(opStack.pop());
        }
        StringBuilder sb = new StringBuilder();
        for (String s : suffixList) {
            sb.append(s).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private int priority(String token){
        if("*".equals(token) || "/".equals(token)){
            return 2;
        }else if("+".equals(token) || "-".equals(token)){
            return 1;
        }else{
            return 0;
        }
    }

    private boolean isOperator(String token){
        return "+".equals(token) || "-".equals(token) || "*".equals(token) || "/".equals(token);
    }

    private boolean isNumber(String token){
        return token.matches("\\d+");
    }

    private String insertBlanks(String expression){
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
        return list.size() == 1 ? list.get(0) : "Failed";

    }

}
