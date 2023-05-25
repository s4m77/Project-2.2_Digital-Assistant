//package nlp;
//
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.Random;
//import java.io.IOException;
//
//public class SpellCheckTest {
//    private static final Random random = new Random();
//
//    public enum Modification {
//        DELETE, INSERT, SUBSTITUTE, SWAP
//    }
////    public static String modifyString(String input) throws IOException {
////        int modification = random.nextInt(4); // random int for each case of modification
////
//////        String modified = switch (modification) {
//////            case 0 -> deleteWord(input);
//////            case 1 -> insertWord(input);
//////            case 2 -> substituteWord(input);
//////            case 3 -> swapWords(input);
//////            default -> "";
//////        };
////        System.out.println("Modification made: " + modification);
////        return null; //modified;
////    }
//
//    public static int performTest(String input, String target, Modification type, int operations){
//        String modified = switch (type) {
//            case SWAP -> swapRandomChar(input);
//            case SUBSTITUTE -> replaceCharInWord(input);
//            case INSERT -> insertCharInWord(input);
//            case DELETE -> deleteRandomChar(input);
//        };
//        return SpellCheck.bestMatch(modified).equals(target) ?
//                performTest(modified, target, type, operations + 1) : operations;
//    }
//
//    //Delete random character from input string and return the modified string
//    private static String deleteRandomChar(String input) {
//        int index = random.nextInt(input.length());
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < input.length(); i++) {
//            if (i != index) {
//                sb.append(input.charAt(i));
//            }
//        }
//        return sb.toString();
//    }
//
//    /**
//     * This method inserts a random character into the input string
//     * @param input String to be modified
//     * @return modified string
//     */
//    private static String insertCharInWord(String input){
//        // select random index of char in word
//        int index = random.nextInt(input.length());
//        // insert a random char at that index
//        char randomChar = (char) (random.nextInt(26) + 'a');
//        return input.substring(0, index) + randomChar + input.substring(index);
//    }
//
//    /**
//     * This method replaces a random character in the input string
//     * @param input String to be modified
//     * @return modified string
//     */
//    private static String replaceCharInWord(String input){
//        // select random index of char in word
//        int index = random.nextInt(input.length());
//        // insert a random char at that index
//        char randomChar = (char) (random.nextInt(26) + 'a');
//        return input.substring(0, index) + randomChar + input.substring(index + 1);
//    }
//
//    private static String swapRandomChar(String input) {
//        int index1 = random.nextInt(input.length());
//        int index2 = random.nextInt(input.length());
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < input.length(); i++) {
//            if (i == index1) {
//                sb.append(input.charAt(index2));
//            } else if (i == index2) {
//                sb.append(input.charAt(index1));
//            } else {
//                sb.append(input.charAt(i));
//            }
//        }
//        return sb.toString();
//    }
//
//    private static String getRandomWord() throws IOException {
//        String fileName = "src/main/resources/NLPdata/Corpus";
//        List<String> corpus =  Files.readAllLines(Paths.get(fileName));;
//        // Convert corpus into array of strings
//        String[] words = corpus.toArray(new String[corpus.size()]);
//        return words[random.nextInt(words.length)];
//    }
//
//    public static void main(String[] args) throws IOException {
////        String text = "I like to eats apple and banana"; // 20 words
////
////        int numTests = 10;
////        int numSimilar = 0;
////        for (int i = 0; i < numTests; i++) {
////            String modified = SpellCheckTest.modifyString(text);
////            String expected = SpellCheck.correctSentence(text);
////            String actual = SpellCheck.correctSentence(modified);
////            if (actual.equals(expected)) {
////                numSimilar++;
////            }
////            System.out.println("Current test:" + i + " *** Nbcorrect : " + numSimilar + " *** Modified sentence: " + modified + " *** Operation performed:");
////        }
////        double percentageCorrect = ((double) numSimilar / numTests) * 100;
////        System.out.println("Percentage correct: " + percentageCorrect + "%");
//
//        for (int i = 0; i < 1000; i++) {
//            System.out.println("Inserting: "+ insertCharInWord("hello"));
//            System.out.println("Replacing: "+ replaceCharInWord("hello"));
//            System.out.println("Swapping: "+ swapRandomChar("hello"));
//            System.out.println("Deleting: "+ deleteRandomChar("hello"));
//
//        }
//    }
//
//}
