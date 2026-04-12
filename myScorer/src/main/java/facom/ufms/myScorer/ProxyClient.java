package facom.ufms.myScorer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ProxyClient implements ScoreClient {

    private record ProxyRequest(String cpf, CompletableFuture<Integer> promisedResponse) { }

    private final APIClient realClient;
    private final Map<String, Integer> cache = new HashMap<>();
    private final BlockingQueue<ProxyRequest> requestBuffer = new LinkedBlockingQueue<>();//Lista FIFO

    public ProxyClient() {
        realClient = new APIClient();
    }

    @Override
    public synchronized int score(String cpf) {

        return cache.computeIfAbsent(cpf, _ -> {

            CompletableFuture<Integer> promise = new CompletableFuture<>();
            ProxyRequest request = new ProxyRequest(cpf, promise);
            boolean isQueued = requestBuffer.offer(request);

            try {
                return promise.join();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                return -1;
            }

        });

    }

    @Scheduled(fixedDelay = 1000)//1 segundo para cada requisição
    public void scheduler() {

        ProxyRequest request = requestBuffer.poll();

        if (request != null) {
            try {
                request.promisedResponse.complete(realClient.score(request.cpf));//completa a requisição
            } catch (Exception e) {
                request.promisedResponse.completeExceptionally(e);
            }
        }

    }

}
