package facom.ufms.myScorer;

import facom.ufms.myScorer.apiProxy.*;
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

        SpringApplication.run(MyScorerApplication.class, args);

	}

}
