package gridmr;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 50051; // puerto en el que escucha el Master
        MasterService master = new MasterService(port);
        master.start();
        master.blockUntilShutdown();
    }
}
