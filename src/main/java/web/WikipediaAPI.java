package web;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class WikipediaAPI {

    public static void webQuery(String query) {
        String summaryEndpoint = "https://en.wikipedia.org/api/rest_v1/page/summary/";
        String searchEndpoint = "https://en.wikipedia.org/w/api.php?action=query&format=json&list=search&srsearch=";
        String title = "";
        String extract = "";
        //static final int MAX_RESULTS = 10;

        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            URL searchUrl = new URL(searchEndpoint + encodedQuery); // + "&srprop=snippet");
            HttpURLConnection searchConn = (HttpURLConnection) searchUrl.openConnection();
            searchConn.setRequestMethod("GET");
            searchConn.connect();
            int searchResponseCode = searchConn.getResponseCode();

            if (searchResponseCode != 200) {
                throw new RuntimeException("Failed to get data from Wikipedia search API: " + searchResponseCode);
            } else {
                Scanner searchScanner = new Scanner(searchUrl.openStream());
                String searchContent = searchScanner.useDelimiter("\\Z").next();

                JSONObject searchJson = new JSONObject(searchContent);
                JSONArray searchResults = searchJson.getJSONObject("query").getJSONArray("search");

                if (searchResults.length() > 0) {
                    title = searchResults.getJSONObject(0).getString("title");
                }
            }

            if (!title.isEmpty()) {
                URL summaryUrl = new URL(summaryEndpoint + URLEncoder.encode(title.replace(" ", "_"), "UTF-8"));
                HttpURLConnection summaryConn = (HttpURLConnection) summaryUrl.openConnection();
                summaryConn.setRequestMethod("GET");
                summaryConn.connect();
                int summaryResponseCode = summaryConn.getResponseCode();

                if (summaryResponseCode != 200) {
                    throw new RuntimeException("Failed to get data from Wikipedia summary API: " + summaryResponseCode);
                } else {
                    Scanner summaryScanner = new Scanner(summaryUrl.openStream());
                    String summaryContent = summaryScanner.useDelimiter("\\Z").next();

                    JSONObject summaryJson = new JSONObject(summaryContent);
                    extract = summaryJson.getString("extract");
                }
            } else {
                extract = "No potential results found";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(extract);
    }

    public static void handleInput(){
        System.out.println("Enter your query:");
        Scanner myObj = new Scanner(System.in);

        String query = myObj.nextLine();
        webQuery(query);

    }

    public static void main(String[] args) {

        String query = "what is the partition of an interval in mathematics?";
        webQuery(query);

    }
}
