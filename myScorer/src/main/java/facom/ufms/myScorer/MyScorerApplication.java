package facom.ufms.myScorer;

import facom.ufms.myScorer.apiProxy.ProxyClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableScheduling
public class MyScorerApplication {

    static void main(String[] args) {

        // 1. Arrancamos a aplicação Spring e guardamos o "contexto"
        ApplicationContext context = SpringApplication.run(MyScorerApplication.class, args);

        // 2. Pedimos ao Spring a instância do ProxyClient que ele está a gerir (e onde o Scheduler está a correr)
        ProxyClient client = context.getBean(ProxyClient.class);

        // 3. O seu código de teste original com 5 Threads simultâneas
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for(int i = 0; i < 5; i++) {
            int callerNumber = i;
            executor.submit(() -> {
        /*######Teste sem cache######################################################*/
                int score = client.score("736.489.210-96");
                if (score > -1)
                    System.out.printf("Score from caller Nº%d - %d\n", callerNumber, score);
                score = client.score("837.968.450-88");
                if (score > -1)
                    System.out.printf("Score from caller Nº%d - %d\n", callerNumber, score);
                score = client.score("734.055.940-06");
                if (score > -1)
                    System.out.printf("Score from caller Nº%d - %d\n", callerNumber, score);
                score = client.score("514.790.640-17");
                if (score > -1)
                    System.out.printf("Score from caller Nº%d - %d\n", callerNumber, score);
        /*######Teste com cache######################################################*/
                /*for(int j = 0; j < 2; j++){
                    // Atenção: Isto vai bloquear aqui até que o Scheduler processe este pedido
                    int score = client.score("837.968.450-88");
                    if (score > -1)
                        System.out.printf("Score from caller Nº%d - %d\n", callerNumber, score);
                }
                for(int k = 0; k < 2; k++) {
                    int score = client.score("736.489.210-96");
                    if (score > -1)
                        System.out.printf("Score from caller Nº%d - %d\n", callerNumber, score);
                }*/
            });
        }

        executor.shutdown();
        try {
            // IMPORTANTE: Como tem 5 threads a fazer 4 pedidos cada, são 20 pedidos no total.
            // A uma taxa de 1 pedido por segundo, isto vai demorar pelo menos 20 segundos a concluir.
            // Por isso, aumentei o timeout para 60 segundos.
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
	}

}
