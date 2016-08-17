package com.bothmena.dialog;

import com.bothmena.entity.Depart;
import com.bothmena.entity.Poste;
import com.bothmena.main.Main;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.util.VetoException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Objects;
import java.util.Optional;
import static com.bothmena.dialog.DialogSetter.setButtons;
import static com.bothmena.dialog.DialogSetter.setDialog;
import static com.bothmena.main.SideMenuController.updateContent;

public class PosteDialog {

    private SessionFactory sessionFactory = Main.getSessionFactory();

    @FXML private JFXTextField name = new JFXTextField();
    @FXML private JFXComboBox<String> type = new JFXComboBox<>();
    @FXML private JFXTextField marque = new JFXTextField();
    @FXML private JFXTextField ptr = new JFXTextField();
    @FXML private JFXCheckBox teleBcc = new JFXCheckBox();
    @FXML private JFXTextArea observation = new JFXTextArea();
    @FXML private JFXComboBox<Poste> pAvant = new JFXComboBox<>();
    @FXML private Label logLabel = new Label("");

    private ButtonType save = new ButtonType("Sauvegarder", ButtonBar.ButtonData.OK_DONE);
    private ButtonType cancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
    private Dialog<String> dialog = new Dialog<>();
    private static Poste firstPoste;
    private Poste poste;
    private Depart depart;
    private boolean newEntry = false;
    private static Poste first = new Poste( "Mettre au Début de Depart" );

    public String showDialog (BorderPane borderPane, Depart depart, Poste poste ) {

        setDialog(dialog, borderPane);
        setButtons(dialog, save, cancel);
        this.depart = depart;
        if (poste == null) {
            newEntry = true;
            setData();
        } else {
            this.poste = poste;
            setData();
            setFormData();
        }
        dialog.setResultConverter(this::resultConverter);
        final Button submitButton = (Button) dialog.getDialogPane().lookupButton(save);
        submitButton.addEventFilter(ActionEvent.ACTION, event -> {
            String result = isPosteValid();
            if ( result != "yes" ){
                event.consume();
                logLabel.setText(result);
            }
        });

        Optional<String> result = dialog.showAndWait();

        if ( result.isPresent() )
            return result.get();
        else
            return null;
    }

    private void setFormData() {

        name.setText( poste.getName() );
        type.setValue( poste.getType() );
        marque.setText( poste.getMarque() );
        ptr.setText( poste.getPtr() );
        teleBcc.setSelected( poste.getTele() );
        observation.setText( poste.getObservation() );
        pAvant.setValue( poste.getPrevPoste() == null ? first : poste.getPrevPoste() );
    }

    private void setData () {

        ObservableList<String> types = FXCollections.observableArrayList();
        types.addAll( "Classique", "CP" );
        type.setItems(types);

        ObservableList<Poste> postes = FXCollections.observableArrayList();

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
//        firstPoste = (Poste) session.createQuery("from com.bothmena.entity.Poste p where p.prevPoste = null and p.depart = depart").getSingleResult();
        depart = session.get( Depart.class, depart.getId() );
        postes.add( first );
        if ( this.poste == null )
            postes.addAll( depart.getPostes() );
        else {
            for ( Poste poste : depart.getPostes() ) {
                if ( poste.getId() != this.poste.getId() )
                    postes.add( poste );
            }
        }
        pAvant.setItems( postes );
        session.close();
    }

    private String resultConverter( ButtonType buttonType ) {

        if ( buttonType == save) {

            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();

            if ( newEntry )
                poste = new Poste();
            else
                poste = session.get( Poste.class, poste.getId() );
            poste.setName( name.getText() );
            poste.setType( type.getValue() );
            poste.setPtr( ptr.getText() );
            poste.setTele( teleBcc.isSelected() );
            poste.setMarque( marque.getText() );
            poste.setObservation( observation.getText() );
            poste.setDepart( depart );

            Poste prevPoste = pAvant.getValue();
            if (Objects.equals( prevPoste, first )) {
                poste.setPrevPoste( null );
                poste.setNextPoste( null );
            } else {
                Poste nextPoste = prevPoste.getNextPoste();
                poste.setPrevPoste( prevPoste );
                poste.setNextPoste( nextPoste );
                prevPoste.setNextPoste( poste );
                if ( nextPoste != null )
                    nextPoste.setPrevPoste(poste);
            }
            if ( newEntry )
                session.save( poste );
            session.getTransaction().commit();
            session.close();
            try {
                updateContent();
            } catch (VetoException | FlowException e) {
                System.out.println(e);
            }
            return "SAVED";
        }else
            return "CANCELLED";
    }

    private String isPosteValid() {

        if ( name.getText().trim().isEmpty() )
            return "Le nom du poste ne doit pas etre vide.";
        else if ( ptr.getText().trim().isEmpty() )
            return "Le P. TR du poste doit doit etre precisé.";
        else if ( type.getValue() != "Classique" && type.getValue() != "CP" )
            return "Le Type du poste doit etre 'Classique' ou 'CP'.";
        else if ( pAvant.getValue() == null )
            return "Tu doit preciser la position du poste (choisir la poste precedante)";

        return "yes";
    }
}
