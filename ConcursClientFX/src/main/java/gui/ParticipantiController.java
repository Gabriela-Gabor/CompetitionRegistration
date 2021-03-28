package gui;

import concurs.model.Copil;
import concurs.model.Utilizator;
import concurs.services.ConcursException;
import concurs.services.IConcursService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

public class ParticipantiController{

    private Stage stage;
    private IConcursService server;
    private Utilizator utilizator;
    ObservableList<Copil> modelCopii = FXCollections.observableArrayList();

    @FXML
    ChoiceBox<String> probaBox;
    @FXML
    ChoiceBox<String> categorieBox;
    @FXML
    ListView<Copil> listaParticipanti;


    public void CancelButton(ActionEvent actionEvent) {
        stage.close();
    }


    public void setServer(Stage stage, IConcursService service) {
        this.stage = stage;
        this.server=service;
    }

    public void setUtilizator(Utilizator u) {
        this.utilizator = u;
    }

    @FXML
    public void cauta() throws ConcursException {
        String proba = probaBox.getValue();
        String categorie = categorieBox.getValue();
        if (categorie!=null && proba!=null) {
            String varsta[] = categorie.split("-");
            int varstaMinima = Integer.parseInt(varsta[0]);
            List<Copil> copii = server.cauta(proba, varstaMinima);
            if(copii.size()>0) {
                modelCopii.setAll(copii);
                listaParticipanti.setItems(modelCopii);
            }
            else
                MessageAlert.showWarningMessage(null, "Nu sunt participanti la aceasta proba!");
        } else
            MessageAlert.showWarningMessage(null, "Alegeti o proba si o categorie de varsta!");

    }
}
