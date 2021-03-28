package concurs.network.utils;

import concurs.network.rpcprotocol.ConcursClientRpcWorker;
import concurs.services.IConcursService;

import java.net.Socket;

public class ConcursRpcConcurrentServer extends AbsConcurrentServer{

    private IConcursService concursServer;

    public ConcursRpcConcurrentServer(int port, IConcursService concursServer) {
        super(port);
        this.concursServer = concursServer;
    }
    @Override
    protected Thread createWorker(Socket client)
    {
        ConcursClientRpcWorker worker=new ConcursClientRpcWorker(concursServer,client);

        Thread tw=new Thread(worker);
        return tw;
    }
    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}

