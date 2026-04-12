package facom.ufms.myScorer;

public class ControlledClient extends DecoratorClient{

    public ControlledClient(ScoreClient client) {
        super(client);
    }

    @Override
    public int score(String cpf) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.getLogger(ControlledClient.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
        }

        return this.client.score(cpf);

    }

}
