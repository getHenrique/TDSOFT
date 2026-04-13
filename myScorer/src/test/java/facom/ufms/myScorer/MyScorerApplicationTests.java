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
        IO.println("Inicindo Rajada controlada:");
        for(int i = 0; i < 5; i++) {
            //20 requisições seguidas, as 4 primeiras não estão em cache, as restantes são devolvidas por cache
            IO.println(proxyClient.score("736.489.210-96"));
            IO.println(proxyClient.score("837.968.450-88"));
            IO.println(proxyClient.score("734.055.940-06"));
            IO.println(proxyClient.score("514.790.640-17"));
        }
    }

    @Test
    void testePenalidadeProposital() {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        //Sem Proxy:
        IO.println("Sem proxy:");
        for(int i = 0; i < 2; i++) {
            executor.submit(() -> {
                IO.println(apiClient.score("736.489.210-96"));
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
