package nlp.distance;

public class Qwerty {

    public static void main(String[] args){

        String s1 = "hello";
        String s2 = "helmo";
        int qwertyDistance = Qwerty.calculate(s1, s2);
        System.out.println(qwertyDistance);

    }

    // QWERTY keyboard layout
    private static final String[] KEYBOARD = {
            "`1234567890-=",
            " qwertyuiop[]\\",
            " asdfghjkl;'",
            " zxcvbnm,./"
    };

    // Calculate the distance between two characters on the keyboard
    private static int distance(char c1, char c2) {
        int x1 = -1, y1 = -1, x2 = -1, y2 = -1;
        for (int i = 0; i < KEYBOARD.length; i++) {
            int index = KEYBOARD[i].indexOf(c1);
            if (index != -1) {
                x1 = index;
                y1 = i;
            }
            index = KEYBOARD[i].indexOf(c2);
            if (index != -1) {
                x2 = index;
                y2 = i;
            }
        }
        if (x1 == -1 || x2 == -1) {
            return Integer.MAX_VALUE;
        }
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    //  Calculate the QWERTY distance between two strings
    public static int calculate(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();
        int[][] dis = new int[len1 + 1][len2 + 1];
        for (int i = 0; i <= len1; i++) {
            dis[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dis[0][j] = j;
        }
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = distance(str1.charAt(i - 1), str2.charAt(j - 1));
                dis[i][j] = Math.min(dis[i - 1][j - 1] + cost, Math.min(dis[i - 1][j] + 1, dis[i][j - 1] + 1));
            }
        }
        return dis[len1][len2];
    }

}

