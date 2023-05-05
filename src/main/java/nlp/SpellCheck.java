package nlp;
public class SpellCheck {
    public static void main(String[] args){
        System.out.println(minEditDistance("apple","p"));
    }
    public static int minEditDistance(String str1, String str2) {
        int l = str1.length();
        int m = str2.length();
        int[][] editDist = new int[l+1][m+1];
        for (int i = 0; i <= l; i++) {
            for (int j = 0; j <= m; j++) {
                if (i == 0)
                    editDist[i][j] = j;
                else if (j == 0)
                    editDist[i][j] = i;
                else if (str1.charAt(i-1) == str2.charAt(j-1))
                    editDist[i][j] = editDist[i-1][j-1];
                else if (i>1 && j>1  && str1.charAt(i-1) == str2.charAt(j-2)
                        && str1.charAt(i-2) == str2.charAt(j-1))
                    editDist[i][j] = 1+Math.min(Math.min(editDist[i-2][j-2], editDist[i-1][j]), Math.min(editDist[i][j-1], editDist[i-1][j-1]));
                else
                    editDist[i][j] = 1 + Math.min(editDist[i][j-1], Math.min(editDist[i-1][j], editDist[i-1][j-1]));
            }
        }
        return editDist[l][m];
    }
}