package concurs.network.rpcprotocol;

import concurs.model.Copil;
import concurs.model.Inregistrare;
import concurs.model.Proba;
import concurs.model.Utilizator;
import concurs.network.dto.InregistrareDTO;
import concurs.services.ConcursException;
import concurs.services.IConcursObserver;
import concurs.services.IConcursService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConcursServicesRpcProxy implements IConcursService {

    private String host;
    private int port;

    private IConcursObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ConcursServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    private void initializeConnection() throws ConcursException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws ConcursException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ConcursException("Error sending object " + e);
        }

    }

    private Response readResponse() throws ConcursException {
        Response response = null;
        try {

            response = qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private class ReaderThread implements Runnable {
        @Override
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (((Response) response).type() == ResponseType.PARTICIPANT_NOU) {
                        handleUpdate((Response) response);
                    } else {
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }

            }
        }
    }

    public void login(Utilizator user, IConcursObserver client) throws ConcursException {
        initializeConnection();
        Request req = new Request.Builder().type(RequestType.LOGIN).data(user).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.client = client;
            return;
        }
        if (response.type() == ResponseType.ERROR) {
            closeConnection();
            throw new ConcursException("Error");
        }
    }

    public void logout(Utilizator user, IConcursObserver client) throws ConcursException {
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(user).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            throw new ConcursException("Error");
        }
    }

    @Override
    public void salveazaCopil(String nume, int varsta) throws ConcursException {

        Copil copil = new Copil(nume, varsta);
        Request req = new Request.Builder().type(RequestType.SAVE_COPIL).data(copil).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            throw new ConcursException("Error");
        }

    }

    @Override
    public void inregistreaza(String nume, int varsta, String proba) throws ConcursException {
        InregistrareDTO inregistrareDTO = new InregistrareDTO(nume, varsta, proba);
        Request req = new Request.Builder().type(RequestType.SAVE_INREGISTRARE).data(inregistrareDTO).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            throw new ConcursException("Error");
        }

    }


    @Override
    public List<Copil> cauta(String proba, int varsta) throws ConcursException {
        String s = proba + "-" + varsta;
        Request req = new Request.Builder().type(RequestType.GET_COPII).data(s).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            throw new ConcursException("Error");
        }
        List<Copil> copii = (List<Copil>) response.data();
        return copii;
    }

    @Override
    public List<Proba> findProbe() {
        return null;
    }

    @Override
    public List<String> findProbeParticipanti() throws ConcursException {
        Request req = new Request.Builder().type(RequestType.GET_PROBE).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            throw new ConcursException("Error");
        }
        List<String> probe = (List<String>) response.data();
        return probe;
    }

    private void handleUpdate(Response response) {
        if ((response.type() == ResponseType.PARTICIPANT_NOU)) {

            Inregistrare inregistrare = (Inregistrare) response.data();
            try {
                client.participantSalvat(inregistrare);
            } catch (ConcursException e) {
                e.printStackTrace();
            }
        }
    }
}

