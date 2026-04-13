package facom.ufms.myScorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyScorerApplication {

    static void main(String[] args) {

        //Rodando na porta 8080
        //Métricas em /actuator/metrics obs: não está funcionando para mim :(
        //Health check em /actuator/health obs: não está funcionando para mim :(
        SpringApplication.run(MyScorerApplication.class, args);

	}

}
