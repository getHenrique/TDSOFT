package facom.ufms.myScorer;

public class ProxyClient implements ScoreClient {

    protected APIClient realClient;

    public ProxyClient(APIClient realClient) {
        this.realClient = realClient;
    }

    @Override
    public int score(String cpf) {
        return realClient.score(cpf);
    }

}
