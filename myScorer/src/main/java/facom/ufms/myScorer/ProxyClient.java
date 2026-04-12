package facom.ufms.myScorer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ProxyClient implements ScoreClient {

    private final APIClient realClient;
    private final AtomicLong lastCallTime = new AtomicLong(0);
    private final Map<String, Integer> cache = new HashMap<>();

    public ProxyClient() {
        realClient = new APIClient();
    }

    @Override
    public synchronized int score(String cpf) {
        return cache.computeIfAbsent(cpf, _ -> {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastCallTime.get()) <= 1000) {
                IO.println("Woah there friend! You may need to slow down!");
                try {
                    for (int i = 1; i < 3; i++) {
                        Thread.sleep(1000);
                        IO.println(i + " seconds for you");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return -1;
            }
            int response = realClient.score(cpf);
            lastCallTime.set(System.currentTimeMillis());
            return response;
        });
    }

}
