package nlp.distance;

import org.apache.commons.text.similarity.CosineDistance;

public class Cosine {

    public static double calculate(String str1, String str2) {
        CosineDistance cosineDistance = new CosineDistance();
        return cosineDistance.apply(str1, str2);
    }

}
