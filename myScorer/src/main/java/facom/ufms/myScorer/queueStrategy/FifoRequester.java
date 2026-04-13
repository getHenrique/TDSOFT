package facom.ufms.myScorer.queueStrategy;

import facom.ufms.myScorer.apiProxy.ProxyRequest;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component("FIFO")
public class FifoRequester implements QueueStrategy {

    private final BlockingQueue<ProxyRequest> requestQueue = new LinkedBlockingQueue<>();

    @Override
    public boolean offerRequest(ProxyRequest request) {
        return requestQueue.offer(request);
    }

    @Override
    public ProxyRequest poolRequest() {
        return requestQueue.poll();
    }

}
