package facom.ufms.myScorer.queueStrategy;

import facom.ufms.myScorer.apiProxy.ProxyRequest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

//@Component//preciso fazer atualizações para não ter que mudar isso manualmente sempre...
public class PriorityRequester implements QueueStrategy {

    private final BlockingQueue<ProxyRequest> requestQueue = new PriorityBlockingQueue<>();

    @Override
    public boolean offerRequest(ProxyRequest request) {
        return requestQueue.offer(request);
    }

    @Override
    public ProxyRequest poolRequest() {
        return requestQueue.poll();
    }

}
