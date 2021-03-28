package gui;

import concurs.model.Utilizator;
import concurs.services.ConcursException;
import concurs.services.IConcursObserver;
import concurs.services.IConcursService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class UtilizatorController implements Initializable, IConcursObserver {

    private Stage stage;
    private IConcursService server;
    private Utilizator utilizator;
    ObservableList<String> modelProbe = FXCollections.observableArrayList();

    @FXML
    TextField numeTextField;
    @FXML
    TextField varstaTextField;
    @FXML
    TextField proba1TextField;
    @FXML
    TextField proba2TextField;
    @FXML
    ListView<String> listaProbe;


    public UtilizatorController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void LogoutButton(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Stage primaryStage = new Stage();
            Parent root = loader.load();
            LoginController ctrl = loader.getController();
            ctrl.setServer(primaryStage, server);
            primaryStage.setScene(new Scene(root, 380, 350));
            primaryStage.show();
            logout();
            stage.close();

        } catch (Exception e) {
            MessageAlert.showWarningMessage(null, "Eroare la deschiderea aplicatiei");
        }

    }


    public void setServer(Stage stage, IConcursService server) {
        this.stage = stage;
        this.server = server;
    }

    public void setUtilizator(Utilizator u) {
        this.utilizator = u;
    }


    public void setProbe() {
        listaProbe.setItems(modelProbe);
        try {
            modelProbe.setAll(server.findProbeParticipanti());
        } catch (ConcursException e) {
            e.printStackTrace();
        }
    }

    public void SearchButton(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/participanti.fxml"));
            Stage stage2 = new Stage();
            Parent root = loader.load();
            ParticipantiController ctrl = loader.getController();
            ctrl.setServer(stage2, server);
            ctrl.setUtilizator(utilizator);
            stage2.setScene(new Scene(root, 530, 310));
            stage2.show();


        } catch (Exception ex) {
            MessageAlert.showWarningMessage(null, "Eroare la deschiderea ferestrei");

        }

    }


    public void clear() {
        numeTextField.clear();
        varstaTextField.clear();
        proba1TextField.clear();
        proba2TextField.clear();
    }

    void logout() {
        try {
            server.logout(utilizator, this);
        } catch (ConcursException e) {
            System.out.println("Logout error " + e);
        }
    }

        @FXML
        public void inscriere () {
        /*String nume = numeTextField.getText();
        int varsta;
        try {
            varsta = Integer.parseInt(varstaTextField.getText());
            String proba1 = proba1TextField.getText();
            String proba2 = proba2TextField.getText();

            if (nume.equals("")) {
                MessageAlert.showWarningMessage(null, "Trebuie introdus un nume");
            } else if (proba1 == "" && proba2 == "") {
                MessageAlert.showWarningMessage(null, "Trebuie sa introduceti macar o proba");
            } else {
                server.salveazaCopil(nume, varsta);
                if (nume != "" && !proba1.equals("")) {
                    if (!server.inregistreaza(nume, varsta, proba1))
                        MessageAlert.showWarningMessage(null, "Inregistrarea a esuat!");
                    else
                        clear();

                }
                if (nume != "" && !proba2.equals("")) {
                    if (!server.inregistreaza(nume, varsta, proba2))
                        MessageAlert.showWarningMessage(null, "Inregistrarea a esuat!");
                    else
                        clear();
                }
                modelProbe.setAll(server.findProbeParticipanti());
            }
        } catch (NumberFormatException e) {
            MessageAlert.showWarningMessage(null, "Varsta trebuie sa fie un nr intreg!");
        }
        */
        }

    }

