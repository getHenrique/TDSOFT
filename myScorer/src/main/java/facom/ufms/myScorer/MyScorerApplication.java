package facom.ufms.myScorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyScorerApplication {

    static void main(String[] args) {

        SpringApplication.run(MyScorerApplication.class, args);

	}

}
