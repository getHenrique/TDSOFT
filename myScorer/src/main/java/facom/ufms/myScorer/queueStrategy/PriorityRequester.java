package facom.ufms.myScorer.queueStrategy;

import facom.ufms.myScorer.apiProxy.ProxyRequest;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

@Component("priorityStrategy")
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
