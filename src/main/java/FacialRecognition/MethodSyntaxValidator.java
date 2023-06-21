package FacialRecognition;
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

    public static void main(String[] args) {
        // Example usage
        String method1 = "public static void printHello() {\n    System.out.println(\"Hello!\");\n}";
        System.out.println(isValidMethod(method1));  // Output: true

        String method2 = "public static int addNumbers(int a, int b) {\n    return a + b;\n}";
        System.out.println(isValidMethod(method2));  // Output: true

        String method3 = "invalid method";
        System.out.println(isValidMethod(method3));  // Output: false
    }
}