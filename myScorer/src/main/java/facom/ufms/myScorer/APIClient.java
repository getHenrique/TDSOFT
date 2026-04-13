package facom.ufms.myScorer;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

//@Service
public class APIClient implements ScoreClient {

    @Override
    public int score(String cpf) {

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://score.hsborges.dev/api/score?cpf=" +  cpf;
        HttpHeaders headers = new HttpHeaders();

        headers.set("client-id", "1");
        headers.set("accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {

            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
            JsonNode jsonBody = response.getBody();

            assert jsonBody != null && jsonBody.has("score");//talvez deveria tratar isso
            return jsonBody.get("score").asInt();

        } catch (RestClientResponseException e) {

            IO.println(
                "API request failed.\n" +
                "Status: " + e.getStatusCode().value() +
                "\n" +
                e.getResponseBodyAsString()
            );

            return -1;

        }

    }

}
