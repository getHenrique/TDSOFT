package facom.ufms.myScorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class MyScorerApplication {

    static void main(String[] args) {

        final var client = new ProxyClient();

//#########Teste de Requisições concorrentes########################################
 /*       //5 Threads para trabalhar de modo concorrente
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Cada uma das 5 Threads chama o score seis vezes
        for(int i = 0; i < 5; i++) {
            int callerNumber = i;
            executor.submit(() -> {
                for(int j = 0; j < 3; j++){
                    int score = client.score("837.968.450-88");
                    if (score > -1)
                        System.out.printf(
                                "Score from caller Nº%d - %d\n",
                                callerNumber,
                                score
                        );
                }
                for(int k = 0; k < 3; k++) {
                    int score = client.score("736.489.210-96");
                    if (score > -1)
                        System.out.printf(
                                "Score from caller Nº%d - %d\n",
                                callerNumber,
                                score
                        );
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
*/

//#########Teste de Requisições sequenciais########################################
/*        for(int j = 0; j < 3; j++){
            int score = client.score("736.489.210-96");
            if (score > -1)
                System.out.printf("Score - %d\n", score);
        }
//        try {//O cache funciona bem demais, usar esta pausa ajuda ao teste de outro cpf não ficar preso
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
        for(int j = 0; j < 3; j++){
            int score = client.score("837.968.450-88");
            if (score > -1)
                System.out.printf("Score - %d\n", score);
        }*/

		//SpringApplication.run(MyScorerApplication.class, args);
	}

}
