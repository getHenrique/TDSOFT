package facom.ufms.myScorer;

import facom.ufms.myScorer.apiProxy.ProxyClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class MyScorerApplicationTests {

    @Autowired
    private ProxyClient proxyClient;
    @Autowired
    private APIClient apiClient;

    @Test
    void testeRajadaControlada() {
        IO.println("###########Inicindo Rajada controlada:################");
        for(int i = 0; i < 5; i++) {
            //20 requisições seguidas,
            // as 4 primeiras não estão em cache e vão para o servidor,
            // já as restantes são devolvidas por cache, pois já foram computadas
            proxyClient.score("736.489.210-96");
            proxyClient.score("837.968.450-88");
            proxyClient.score("734.055.940-06");
            proxyClient.score("514.790.640-17");
        }
    }

    @Test
    void testePenalidadeProposital() {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IO.println("######################Sem proxy:##########################");
        for(int i = 0; i < 2; i++) {
            executor.submit(() -> {
                apiClient.score("736.489.210-96");
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
