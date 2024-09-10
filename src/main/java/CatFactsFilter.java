import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CatFactsFilter {
    public static void main(String[] args) {
        String url = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
        List<CatFact> catFacts = fetchAndFilterCatFacts(url);
        catFacts.forEach(System.out::println);
    }

    private static List<CatFact> fetchAndFilterCatFacts(String url) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                ObjectMapper mapper = new ObjectMapper();
                CatFact[] facts = mapper.readValue(response.getEntity().getContent(), CatFact[].class);
                return Arrays.stream(facts)
                        .filter(f -> f.getUpvotes() != null && f.getUpvotes() > 0)
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error fetching and filtering cat facts", e);
        }
    }
}