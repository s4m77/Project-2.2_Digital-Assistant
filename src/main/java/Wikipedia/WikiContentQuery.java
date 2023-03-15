package Wikipedia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikiContentQuery {


    public static void main(String[] args) throws Exception {
        extractContentByKeyword("Belgium");
    }

    public static void extractContentByKeyword(String keyword) throws Exception {
        Document document = getURLByKeyword(keyword);
        Elements content = document.select(".mw-content-ltr p, .mw-content-ltr li");

        Element contentFirst = content.first();
        Element contentLast = content.last();

        assert contentFirst != null;

        for (int i = 1; i < content.size(); i++) {
            Element element = content.get(i);
            System.out.println(element.text());
            if (element == contentLast) {
                break;
            }
        }
    }

    public static Document getURLByKeyword(String keyword) throws Exception {
        String wikiUrl = "https://en.wikipedia.org/wiki/" + keyword;
        return Jsoup.connect(wikiUrl).get();
    }

}