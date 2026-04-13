package facom.ufms.myScorer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proxy")
public class ProxyController {// Controller como interface do cliente HTTP

    private final ScoreClient proxyClient;

    public ProxyController(ScoreClient proxyClient) {
        this.proxyClient = proxyClient;
    }

    @GetMapping("/score")
    public ResponseEntity<Integer> fetchScore(@RequestParam("cpf") String cpf) {

        int score = proxyClient.score(cpf);

        if(score == -1) return ResponseEntity.internalServerError().build();

        return ResponseEntity.ok(score);

    }

}
