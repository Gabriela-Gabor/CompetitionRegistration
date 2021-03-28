package concurs.network.rpcprotocol;

import concurs.model.Copil;
import concurs.model.Utilizator;
import concurs.network.utils.ServerException;
import concurs.services.ConcursException;
import concurs.services.IConcursObserver;
import concurs.services.IConcursService;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ConcursClientRpcWorker implements Runnable, IConcursObserver {

    private IConcursService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ConcursClientRpcWorker(IConcursService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();
    private static Response errorResponse = new Response.Builder().type(ResponseType.ERROR).build();

    private Response handleRequest(Request request) {
        Response response = null;
        if (request.type() == RequestType.LOGIN) {
            Utilizator u = (Utilizator) request.data();
            try {
                server.login(u, this);
                return okResponse;
            } catch (ConcursException e) {
                connected = false;
                return errorResponse;
            }
        }
        if (request.type() == RequestType.LOGOUT) {
            Utilizator u = (Utilizator) request.data();
            try {
                server.logout(u, this);
                connected = false;
                return okResponse;

            } catch (ConcursException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.GET_PROBE) {
            try {
                List<String> probe = server.findProbeParticipanti();
                return new Response.Builder().type(ResponseType.GET_PROBE).data(probe).build();
            } catch (ConcursException e) {
                return errorResponse;
            }

        }
        if (request.type() == RequestType.GET_COPII)
            try {
                String s = (String) request.data();
                String[] fields = s.split("-");
                List<Copil> copii = server.cauta(fields[0], Integer.parseInt(fields[1]));
                return new Response.Builder().type(ResponseType.GET_COPII).data(copii).build();
            } catch (ConcursException e) {
                return errorResponse;
            }
        return response;
    }

    private void sendResponse(Response response) throws IOException {
        output.writeObject(response);
        output.flush();
    }
}

