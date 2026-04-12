package facom.ufms.myScorer.apiProxy;

import java.util.concurrent.CompletableFuture;

public record ProxyRequest(String cpf, CompletableFuture<Integer> promisedResponse) { }