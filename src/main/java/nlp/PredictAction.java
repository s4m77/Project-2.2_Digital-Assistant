package nlp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

public class PredictAction {
    public static String PredictAction(String userInput) {
        String label = "";
        String score = "";
        try {
            // encode user input with UTF-8 format (necessary for URL request)
            String encodedUserInput = URLEncoder.encode(userInput, "UTF-8");
            String url = "http://localhost:8000/predict?text=%22" + encodedUserInput + "%22";

            // Url object --> initated
            URL apiUrl = new URL(url);

            // open the connection
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // set the get request method
            connection.setRequestMethod("GET");

            // retrieve response code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());

                // Extract field -> "label"
                label = jsonResponse.getString("label");
                score = jsonResponse.getString("score");
                // print
                System.out.println("Label: " + label);
                System.out.println("Score: " + score);

            } else {
                System.out.println("Request failed with response code: " + responseCode);
            }

            // close the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return label;
    }

    public static void main(String[] args) {
        String result = PredictAction("is 7 + 3 equal to 10");
    }
}