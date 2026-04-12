package facom.ufms.myScorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyScorerApplication {

    static void main(String[] args) {

        final var apiClient = new CachedClient(new ControlledClient(new APIClient()));

        for(int i = 0; i < 3; i++)
            IO.println(apiClient.score("837.968.450-88"));
        for(int i = 0; i < 3; i++)
            IO.println(apiClient.score("437.551.200-89"));

		//SpringApplication.run(MyScorerApplication.class, args);
	}

}
