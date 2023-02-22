package CFG;

import java.util.ArrayList;

public class test {
    //class meant for testing purposes

    public static void main(String[] args) {
        ArrayList<Boolean> passed = new ArrayList<Boolean>();
        ArrayList<String> test = new ArrayList<String>();
        passed.add(assertMathBasic());
        test.add("assertMathBasic");
        passed.add(assertMathBasic2());
        test.add("assertMathBasic2");
        passed.add(assertMathBasic3());
        test.add("assertMathBasic3");
        passed.add(assertMathBasic4());
        test.add("assertMathBasic4");
        passed.add(assertMathBasic5());
        test.add("assertMathBasic5");
        passed.add(assertMathAdvanced());
        test.add("assertMathAdvanced");
        passed.add(assertMathAdvanced2());
        test.add("assertMathAdvanced2");
        passed.add(assertMultipleDigits());
        test.add("assertMultipleDigits");
        passed.add(assertMultipleDigits2());
        test.add("assertMultipleDigits2");
        passed.add(assertMultipleDigits3());
        test.add("assertMultipleDigits3");
        passed.add(assert3digits());
        test.add("assert3digits");
        passed.add(assert3digits2());
        test.add("assert3digits2");
        passed.add(assertparenthesis());
        test.add("assertparenthesis");
        passed.add(assertparenthesis2());
        test.add("assertparenthesis2");
        passed.add(assertparenthesis3());
        test.add("assertparenthesis3");
        passed.add(assertultimatetest());
        test.add("assertultimatetest");
        for(int i=0;i<passed.size();i++){
            if(passed.get(i)){
                System.out.println("Test "+i+" passed: "+test.get(i));
            }
            else{
                System.out.println("Test "+i+" failed: "+test.get(i));
            }
        }
        

    }

    public static boolean assertMathBasic(){
        String[] test={"1","*","2"};
        return doMath(test)==2;
    }
    public static boolean assertMathBasic2(){
        String[] test={"1","+","2"};
        return doMath(test)==3;
    }
    public static boolean assertMathBasic3(){
        String[] test={"4","/","2"};
        return doMath(test)==2;
    }	
    public static boolean assertMathBasic4(){
        String[] test={"4","-","2"};
        return doMath(test)==2;
    }
    public static boolean assertMathBasic5(){
        String[] test={"4","^","2"};
        return doMath(test)==16;
    }
    public static boolean assertMathAdvanced(){
        String[] test={"1","*","2","+","3"};
        return doMath(test)==5;
    }
    public static boolean assertMathAdvanced2(){
        String[] test={"1","*","2","+","3","*","4"};
        return doMath(test)==14;
    }
    public static boolean assertMultipleDigits(){
        String[] test={"1","0","*","2","+","3","*","4"};
        return doMath(test)==32;
    }
    public static boolean assertMultipleDigits2(){
        String[] test={"1","0","*","2","+","3","*","4","0","*","5"};
        return doMath(test)==620;
    }
    public static boolean assertMultipleDigits3(){
        String[] test={"1","0","*","2","+","3","*","4","0","*","5","*","6"};
        return doMath(test)==3620;
    }
    public static boolean assert3digits(){
        String[] test={"1","0","0","*","2"};
        return doMath(test)==200;
    }
    public static boolean assert3digits2(){
        String[] test={"1","0","0","*","2","0","0"};
        return doMath(test)==20000;
    }
    public static boolean assertparenthesis(){
        String[] test={"(","1","0","0","+","2",")","*","3"};
        return doMath(test)==306;
    }
    public static boolean assertparenthesis2(){
        String[] test={"(","1","0","0","+","2",")","+","(","3","*","4",")"};
        return doMath(test)==114;
    }
    public static boolean assertparenthesis3(){
        String[] test={"(","1","0","0","+","2",")"};
        return doMath(test)==102;
    }
    public static boolean assertultimatetest(){
        String s="((((2+5)*4)*(3+2))+(2^3)^4)+(2^3)^4+2";
        String[] test=s.split("");
        return doMath(test)==8334;
    }
    public static double doMath(String[] test){
        String rule="Rule <MATHEXPRESSION&> <NUMBER> <OPERATOR> <MATHEXPRESSION> | <MATHEXPRESSION> <OPERATOR> <NUMBER> | <MATHEXPRESSION> <OPERATOR> <MATHEXPRESSION> | ( <MATHEXPRESSION> ) | <NUMBER> <OPERATOR> <NUMBER>";
        Node tree = new Node("top",null);
        CFG.applyRuleChar(tree, rule, test);
        Node branch = tree.getNode("<MATHEXPRESSION>");
        if(branch==null){
            branch = tree.getNode("<MATHEXPRESSION&>");
        }
        if(branch==null){
            return -1;
        }
        String expression = branch.getFilledString().replace(" ", "");
        double answer=TemplateMethods.eval(expression);
        return answer;
    }
    
    
}
