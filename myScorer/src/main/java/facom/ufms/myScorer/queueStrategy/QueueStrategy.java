package facom.ufms.myScorer.queueStrategy;

import facom.ufms.myScorer.apiProxy.ProxyRequest;

public interface QueueStrategy {

    boolean offerRequest(ProxyRequest request);
    ProxyRequest poolRequest();

}
