import concurs.model.Utilizator;
import concurs.network.rpcprotocol.ConcursServicesRpcProxy;
import concurs.services.IConcursService;
import gui.LoginController;
import gui.MessageAlert;
import gui.UtilizatorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class StartRpcClientFX extends Application {
    private Stage primaryStage;

    private static int defaultConcursPort = 55555;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {

        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFX.class.getResourceAsStream("/concursclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("cconcurs.server.host", defaultServer);
        int serverPort = defaultConcursPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("concurs.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultConcursPort);
        }

        IConcursService server=new ConcursServicesRpcProxy(serverIP,serverPort);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            LoginController ctrl = loader.getController();
            ctrl.setServer(primaryStage,server);
            primaryStage.setScene(new Scene(root, 380, 350));
            primaryStage.show();

        } catch (Exception e) {
            MessageAlert.showWarningMessage(null, "Eroare la deschiderea aplicatiei");
        }


    }


}
