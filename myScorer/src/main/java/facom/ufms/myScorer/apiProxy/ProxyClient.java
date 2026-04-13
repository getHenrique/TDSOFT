package facom.ufms.myScorer.apiProxy;

import facom.ufms.myScorer.APIClient;
import facom.ufms.myScorer.ScoreClient;
import facom.ufms.myScorer.queueStrategy.QueueStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Primary
@Service
public class ProxyClient implements ScoreClient {

    private final APIClient realClient;
    private final Map<String, CompletableFuture> cache = new ConcurrentHashMap<>();
    private final QueueStrategy requestBuffer;
    private static final Logger log = LoggerFactory.getLogger(ProxyClient.class);

    //Troque o qualifier para trocar a estratégia
    public ProxyClient(@Qualifier("fifoStrategy") QueueStrategy queueStrategy) {
        requestBuffer = queueStrategy;
        realClient = new APIClient();
    }

    @Override
    public int score(String cpf) {

        CompletableFuture<Integer> future = cache.computeIfAbsent(cpf, _ -> {
            CompletableFuture<Integer> promise = new CompletableFuture<>();
            ProxyRequest request = new ProxyRequest(cpf, promise);
            requestBuffer.offerRequest(request);
            return promise;
        });

        try {
            return future.join();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return -1;
        }

    }

    @Scheduled(fixedDelay = 1000)//1 segundo para cada requisição
    public void scheduler() {

        ProxyRequest request = requestBuffer.poolRequest();

        if (request != null) {
            log.info("Processando fila. Chamando upstream para CPF: {}", request.cpf());
            try {
                int response = realClient.score(request.cpf());
                request.promisedResponse().complete(response);//completa a requisição
                if(response != -1) log.info("Sucesso! Score de {} obtido para CPF: {}", response, request.cpf());
            } catch (Exception e) {
                log.error("Erro ao chamar upstream para o CPF: {}", request.cpf(), e);
                request.promisedResponse().completeExceptionally(e);
            }
        }

    }

}
