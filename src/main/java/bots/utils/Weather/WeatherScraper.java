package bots.utils.Weather;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


public class WeatherScraper {

    private static final String ENDPOINT_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/weatherdata/forecast";
    private static final String API_KEY = "C75MHL3PX4HTAH53UHAL36CTF";

    // Capture the city and country codes that have been queried before.
    private static final Map<String, String> cityCountryCodeMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        //Scanner scanner = new Scanner(System.in);
        //System.out.print("Enter the city name: ");
        //String city = scanner.nextLine();

        System.out.println(WeatherRetriever("aachen"));
    }

    public static String getCountryCode(String city) {
        // check if the country code for the city already exists in the cache
        if (cityCountryCodeMap.containsKey(city)) {
            return cityCountryCodeMap.get(city);
        }

        try {
            // if not, make a network request to obtain the country code.
            String KEY = "2f578e9901bbad407751a75e6c37b63b";
            String URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";
            String url = String.format(URL, city, KEY);
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                String country = jsonObject.getJSONObject("sys").getString("country");
                Locale l = new Locale("", country);
                String countryCode = l.getCountry().toUpperCase();
                // add the result to the cache
                cityCountryCodeMap.put(city, countryCode);
                return countryCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Cannot find country code for " + city;
    }

    public static String WeatherRetriever(String city) throws Exception {
        String countryCode = getCountryCode(city);
        URIBuilder uriBuilder = new URIBuilder(ENDPOINT_URL);
        uriBuilder.setParameter("forecastDays", "0")
                .setParameter("aggregateHours", "24")
                .setParameter("locationMode", "single")
                .setParameter("contentType", "csv")
                .setParameter("unitGroup", "metric")
                .setParameter("key", API_KEY)
                .setParameter("locations", city + "," + countryCode);

        HttpGet httpGet = new HttpGet(uriBuilder.build());

        // create one thread to fetch weather data
        Thread weatherThread = new Thread(new Runnable() {
            public void run() {
                CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
                try (CloseableHttpResponse execute = closeableHttpClient.execute(httpGet)) {
                    if (execute.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                        System.out.println("Received bad response status code:%d%n" + execute.getStatusLine().getStatusCode());
                    }

                    HttpEntity executeEntity = execute.getEntity();
                    if (executeEntity != null) {
                        // Write the response content to a csv file
                        String fileName = "src/main/java/bots/utils/weather/weather.csv";
                        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                        writer.write(EntityUtils.toString(executeEntity, StandardCharsets.UTF_8));
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        AtomicReference<String> sharedResult = new AtomicReference<>("");
        // create one thread to read and write file
        Thread fileThread = new Thread(() -> {
            String fileName = "src/main/java/bots/utils/weather/weather.csv";
            List<String> lines;
            try {
                lines = Files.readAllLines(Paths.get(fileName));
                String[] header = lines.get(1).split("\"");
                String result = header[header.length - 1];
                sharedResult.set(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // execute two threads
        weatherThread.start();
        fileThread.start();

        weatherThread.join();
        fileThread.join();

        return sharedResult.get();
    }

}
