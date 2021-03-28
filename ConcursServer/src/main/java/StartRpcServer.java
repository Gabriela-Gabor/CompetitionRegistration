import concurs.network.utils.AbsConcurrentServer;
import concurs.network.utils.AbstractServer;
import concurs.network.utils.ConcursRpcConcurrentServer;
import concurs.network.utils.ServerException;
import concurs.persistence.ICopiiRepository;
import concurs.persistence.IInregistrariRepository;
import concurs.persistence.IProbeRepository;
import concurs.persistence.IUtilizatoriRepository;
import concurs.persistence.repository.CopiiRepository;
import concurs.persistence.repository.InregistrariRepository;
import concurs.persistence.repository.ProbeRepository;
import concurs.persistence.repository.UtilizatoriRepository;
import concurs.server.ConcursService;
import concurs.services.IConcursService;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {


        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/concursserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find concursserver.properties " + e);
            return;
        }
        IUtilizatoriRepository repoUtilizatori = new UtilizatoriRepository(serverProps);
        ICopiiRepository repoCopii = new CopiiRepository(serverProps);
        IProbeRepository repoProbe = new ProbeRepository(serverProps);
        IInregistrariRepository repoInregistrari = new InregistrariRepository(serverProps, repoCopii, repoProbe);

        IConcursService service = new ConcursService(repoCopii, repoProbe, repoUtilizatori, repoInregistrari);

        int concursServerPort = defaultPort;
        try{
            concursServerPort=Integer.parseInt(serverProps.getProperty("concurs.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }

        AbstractServer server =new ConcursRpcConcurrentServer(concursServerPort,service);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(ServerException e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }

    }
}
