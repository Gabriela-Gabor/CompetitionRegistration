package gui;

import concurs.model.Utilizator;
import concurs.services.ConcursException;
import concurs.services.IConcursService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;
import jdk.jshell.execution.Util;


public class LoginController {

    private Stage stageMain;
    private IConcursService server;
    private UtilizatorController userCtrl;
    private Utilizator crtUser;

    Parent mainParent;


    @FXML
    TextField usernameTextField;

    @FXML
    PasswordField parolaTextField;


    public void setUserController(UtilizatorController uctrl) {
        this.userCtrl = uctrl;
    }

    public LoginController() {
    }

    public void setServer(Stage mStage, IConcursService server) {
        this.stageMain = mStage;
        this.server = server;

    }

    public void LoginButton(ActionEvent actionEvent) {
        String numeUtilizator = usernameTextField.getText();
        String parola = parolaTextField.getText();
        String errors = "";
        if (numeUtilizator.equals("")) {
            errors += "Introdueti un username! ";
        }
        if (parola.equals("")) {
            errors += "Introduceti o parola! ";
        }
        if (!errors.equals("")) {
            MessageAlert.showWarningMessage(null, errors);
        } else {
            crtUser = new Utilizator(numeUtilizator, parola);
            try {
                FXMLLoader uloader = new FXMLLoader(getClass().getResource("/utilizator.fxml"));
                Parent uroot = uloader.load();
                UtilizatorController uctrl = uloader.getController();

                setUserController(uctrl);

                try {
                    server.login(crtUser, userCtrl);

                    Stage stage = new Stage();
                    stage.setTitle("Window for " + crtUser.getNumeUtilizator());
                    stage.setScene(new Scene(uroot, 600, 350));

                    stageMain.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            userCtrl.logout();
                            System.exit(0);
                        }
                    });

                    userCtrl.setServer(stage, server);
                    userCtrl.setProbe();
                    userCtrl.setUtilizator(crtUser);
                    stageMain.close();

                    stage.show();
                } catch (ConcursException e) {
                    MessageAlert.showWarningMessage(null, e.getMessage());
                }
            } catch (Exception ex) {
                MessageAlert.showWarningMessage(null, "Eroare la deschiderea ferestrei");


            }
        }
    }

}

