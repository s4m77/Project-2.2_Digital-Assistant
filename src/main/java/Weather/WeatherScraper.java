//package Weather;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//
//import java.nio.charset.StandardCharsets;
//
//public class WeatherScraper {
//
//    private static final String ENDPOINT_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/weatherdata/forecast";
//    private static final String API_KEY = "C75MHL3PX4HTAH53UHAL36CTF";
//
//    public static void main(String[] args) throws Exception {
//          HourlyWeatherRetriever("Brussels","BE");
//    }
//
//    public static void sendHttpRequest(URIBuilder uriBuilder) throws Exception {
//        HttpGet httpGet = new HttpGet(uriBuilder.build());
//
//        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
//
//        try (CloseableHttpResponse execute = closeableHttpClient.execute(httpGet)) {
//            if (execute.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//                System.out.println("Received bad response status code:%d%n" + execute.getStatusLine().getStatusCode());
//            }
//
//            HttpEntity executeEntity = execute.getEntity();
//            if (executeEntity != null) {
//                System.out.println(EntityUtils.toString(executeEntity, StandardCharsets.UTF_8));
//            }
//
//        }
//    }
//
//    public static void HourlyWeatherRetriever(String city, String countryCode) throws Exception {
//        URIBuilder uriBuilder = new URIBuilder(ENDPOINT_URL);
//        uriBuilder.setParameter("forecastDays", "1")
//                .setParameter("aggregateHours", "1")
//                .setParameter("locationMode", "single")
//                .setParameter("contentType", "csv")
//                .setParameter("unitGroup", "metric")
//                .setParameter("key", API_KEY)
//                .setParameter("locations", city + "," + countryCode);
//
//        sendHttpRequest(uriBuilder);
//    }
//
//}
