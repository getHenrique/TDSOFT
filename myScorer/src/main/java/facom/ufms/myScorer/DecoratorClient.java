package facom.ufms.myScorer;

public abstract class DecoratorClient implements ScoreClient {

    protected ScoreClient client;

    public DecoratorClient(ScoreClient client){
        this.client = client;
    }

    @Override
    public abstract int score(String cpf);

}
