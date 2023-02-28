package Weather;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class WeatherScraper {

    private static final String ENDPOINT_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/weatherdata/forecast";
    private static final String API_KEY = "C75MHL3PX4HTAH53UHAL36CTF";

    public static String getWeather(String city, String country) throws Exception {
        URIBuilder builder = new URIBuilder(ENDPOINT_URL);
        builder.setParameter("aggregateHours", "24")
                .setParameter("contentType", "csv")
                .setParameter("unitGroup", "metric")
                .setParameter("locationMode", "single")
                .setParameter("key", API_KEY)
                .setParameter("locations", city + "," + country);

        return getString(builder);
    }

    private static String getString(URIBuilder builder) throws URISyntaxException, IOException {
        HttpGet get = new HttpGet(builder.build());

        try (CloseableHttpClient httpclient = HttpClients.createDefault();
             CloseableHttpResponse response = httpclient.execute(get)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return "Bad response status code:%d%n" + response.getStatusLine().getStatusCode();
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
        }

        return "";
    }

    public static String getHourlyWeather(String city, String country) throws Exception {
        URIBuilder builder = new URIBuilder(ENDPOINT_URL);
        builder.setParameter("forecastDays", "1")
                .setParameter("aggregateHours", "1")
                .setParameter("contentType", "csv")
                .setParameter("unitGroup", "metric")
                .setParameter("locationMode", "single")
                .setParameter("iconSet", "icons1")
                .setParameter("key", API_KEY)
                .setParameter("locations", city + "," + country);

        return getString(builder);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getWeather("Brussels", "BE"));
        System.out.println("******************************************************************************************************************");
        System.out.println(getHourlyWeather("Maastricht","NL"));
    }
}
