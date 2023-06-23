<<<<<<< HEAD:src/main/java/MethodAdding/MethodSyntaxValidator.java
package MethodAdding;
=======
package FacialRecognition;
>>>>>>> 5bc96043d1081f6e217473fbe205d478966a2f0a:src/main/java/FacialRecognition/MethodSyntaxValidator.java
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class MethodSyntaxValidator {
    public static boolean isValidMethod(String methodString) {
        // Regular expression pattern to match a valid Java method declaration
        String pattern = "^(public\\s+|private\\s+|protected\\s+)?(static\\s+)?[\\w\\<\\>\\[\\]]+\\s+[\\w]+\\s*\\([^\\)]*\\)\\s*(throws\\s+[\\w\\.\\,\\s]+)?\\s*\\{[\\s\\S]*\\}$";

        // Create a Pattern object
        Pattern methodPattern = Pattern.compile(pattern);

        // Create a Matcher object
        Matcher matcher = methodPattern.matcher(methodString);

        // Check if the method matches the pattern
        return matcher.matches();
    }

    public static String ContainsRequirements(String method){
        //assumes the string is a valid method
        //check if the method returns void

        //check if it takes in a Node object as a parameter

        //find the index of the first bracket
        int firstBracket = method.indexOf("(");
        //find index of the first closing bracket
        int firstClosingBracket = method.indexOf(")");
        //create a string of the parameters
        String parameters = method.substring(firstBracket + 1, firstClosingBracket);
        //should only take in a Node object
        String[] parameterArray = parameters.split(" ");
        if(parameterArray.length != 2){
            return "Method takes in more than one parameter";
        }
        if(!parameterArray[0].equals("Node")){
            return "Method does not take in a Node object as a parameter";
        }
        return "Method is valid";
    }

    public static void main(String[] args) {
        // Example usage
        String method1 = "public static void printHello() {\n    System.out.println(\"Hello!\");\n}";
        System.out.println(isValidMethod(method1));  // Output: true

        String method2 = "public static int addNumbers(int a, int b) {\n    return a + b;\n}";
        System.out.println(isValidMethod(method2));  // Output: true

        String method3 = "public static void test() {return \"Hello!\"}}}";
        System.out.println(isValidMethod(method3));  // Output: false

    }
}