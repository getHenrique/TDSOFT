package facom.ufms.myScorer.apiDecorator;

import facom.ufms.myScorer.ScoreClient;

import java.util.Map;
import java.util.HashMap;

public class CachedClient extends DecoratorClient {

    //Agora eu apliquei a lógica de caching :)

    private final Map<String, Integer> cache = new HashMap<>();

    public CachedClient(ScoreClient client) {
        super(client);
    }

    @Override
    public int score(String cpf) {
        return this.cache.computeIfAbsent(cpf, _ -> this.client.score(cpf));
    }

}
