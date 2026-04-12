package facom.ufms.myScorer.apiDecorator;

import facom.ufms.myScorer.ScoreClient;

public abstract class DecoratorClient implements ScoreClient {

    protected ScoreClient client;

    public DecoratorClient(ScoreClient client){
        this.client = client;
    }

    @Override
    public abstract int score(String cpf);

}
