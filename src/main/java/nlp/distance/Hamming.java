package nlp.distance;

public class Hamming {

    public static double calculate(String str1, String str2) {
        double distance = 0;
        for (int i = 0; i < str1.length(); i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                distance++;
            }
        }

        return distance;
    }
}
