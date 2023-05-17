package nlp.distance;

public class Jaro {

    public static double calculate(String str1, String str2) {
        int l = str1.length();
        int m = str2.length();

        if (l == 0 && m == 0) return 1;

        int match_distance = Integer.max(l, m) / 2 - 1;

        boolean[] s_matches = new boolean[l];
        boolean[] t_matches = new boolean[m];

        int matches = 0;
        int transpositions = 0;

        for (int i = 0; i < l; i++) {
            int start = Integer.max(0, i-match_distance);
            int end = Integer.min(i+match_distance+1, m);

            for (int j = start; j < end; j++) {
                if (t_matches[j]) continue;
                if (str1.charAt(i) != str2.charAt(j)) continue;
                s_matches[i] = true;
                t_matches[j] = true;
                matches++;
                break;
            }
        }

        if (matches == 0) return 0;

        int k = 0;
        for (int i = 0; i < l; i++) {
            if (!s_matches[i]) continue;
            while (!t_matches[k]) k++;
            if (str1.charAt(i) != str2.charAt(k)) transpositions++;
            k++;
        }

        return (((double)matches / l) +
                ((double)matches / m) +
                (((double)matches - transpositions/2.0) / matches)) / 3.0;
    }

    public static void main(String[] args) {
        System.out.println(calculate("hello", "hel"));

    }
}