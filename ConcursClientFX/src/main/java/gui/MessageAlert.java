package gui;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MessageAlert {

    public static void showErrorMessage(Stage owner, String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.initOwner(owner);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }
    public static void showWarningMessage(Stage owner, String text){
        Alert message=new Alert(Alert.AlertType.WARNING);
        message.initOwner(owner);
        message.setTitle("Warning");
        message.setContentText(text);
        message.showAndWait();
    }

    public static void showMessage(Stage owner, String text){
        Alert message=new Alert(Alert.AlertType.INFORMATION);
        message.initOwner(owner);
        message.setTitle("Mesaj");
        message.setContentText(text);
        message.showAndWait();
    }
}