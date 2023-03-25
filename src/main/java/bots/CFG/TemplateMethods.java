package bots.CFG;
import static bots.utils.web.WikipediaAPI.webQuery;

public class TemplateMethods {

    public static void Wiki(Node tree){
        try{
            String query=tree.getNode("<WIKIQUERY>").getFilledString();
            String summary=webQuery(query);
            tree.addChild(new Node("<WIKIANSWER>", summary));
        }
        catch(Exception e){
            tree.addChild(new Node("<WIKIANSWER>", "I'm sorry, I don't understand the query inputted."));
        }
    }


    public static void Math(Node tree){
        try{
            Node branch = tree.getNode("<MATHEXPRESSION>");
            String expression = branch.getFilledString().replace(" ", "");
            //tree.addChild(new Node("<ANSWER>", String.valueOf(eval(expression))));
            double answer=eval(expression);
            tree.addChild(new Node("<ANSWER>",String.valueOf(answer)));
        
        }catch(Exception e){
            tree.addChild(new Node("<ANSWER>", "I'm sorry, I don't understand that expression."));
        }
        
    }

    public static void mathQuestion(Node tree){
        try{
            String sentence=tree.getNode("MATHEQUAL").getFilledString();
            String left=sentence.split(" ")[1];
            String right=sentence.split(" ")[4];
            if(eval(left)==eval(right)){
                tree.addChild(new Node("<ANSWER>", "Yes, that is correct."));
            }
            else{
                tree.addChild(new Node("<ANSWER>", "No, that is incorrect."));
            }
        }
        catch(Exception e){
            tree.addChild(new Node("<ANSWER>", "I'm sorry, I don't understand that expression."));
        }
    }


    //blatenly stolen from https://stackoverflow.com/questions/3422673/how-to-evaluate-a-math-expression-given-in-string-form
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;
            
            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }
            
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }
            
            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }
            
            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor
            
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }
            
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }
            
            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus
                
                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    x = switch (func) {
                        case "sqrt" -> Math.sqrt(x);
                        case "sin" -> Math.sin(Math.toRadians(x));
                        case "cos" -> Math.cos(Math.toRadians(x));
                        case "tan" -> Math.tan(Math.toRadians(x));
                        default -> throw new RuntimeException("Unknown function: " + func);
                    };
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }
                
                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation
                
                return x;
            }
        }.parse();
    }
    
    
}
