package com.bothmena.dialog;

import com.bothmena.entity.Depart;
import com.bothmena.entity.Region;
import com.bothmena.main.Main;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.bothmena.main.MainController.*;
import static com.bothmena.main.SideMenuController.fillListView;

/**
 * Created by Aymen Ben Othmen on 30/07/16.
 */

public class DepartDialog {

    private SessionFactory sessionFactory = Main.getSessionFactory();

    @FXML private JFXComboBox<Region> region;
    @FXML private JFXTextField name;
    @FXML private Label logLabel;
    private boolean newEntry = false;

    private Dialog<String> dialog = new Dialog<>();
    private ButtonType save = new ButtonType("Sauvegarder", ButtonBar.ButtonData.APPLY);
    private ButtonType remove = new ButtonType("Supprimer", ButtonBar.ButtonData.OTHER);
    private ButtonType cancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
    private Depart depart;

    public String showDialog( BorderPane borderPane, Depart depart ) {

        DialogSetter.setDialog( dialog, borderPane );
        DialogSetter.setButtons( dialog, save, cancel );
        if( depart == null ) {
            newEntry = true;
        } else {
            this.depart = depart;
            this.region.setValue( this.depart.getRegion() );
            this.name.setText( this.depart.getName() );
        }
        setData();

        final Button submitButton = (Button) dialog.getDialogPane().lookupButton(save);
        submitButton.addEventFilter(ActionEvent.ACTION, event -> {
            String err = isLigneValid();
            if ( "yes" != err ) {
                logLabel.setText( err );
                event.consume();
            }
        });

        dialog.setResultConverter(this::resultConverter);
        Optional<String> result = dialog.showAndWait();

        if ( result.isPresent() )
            return result.get();
        else
            return null;
    }

    private void setData() {

        Session session = sessionFactory.getCurrentSession();
        ObservableList<Region> regions = FXCollections.observableArrayList();

        session.beginTransaction();
        List<Region> regionList = session
                .createQuery("from com.bothmena.entity.Region").list();
        session.close();

        Iterator<Region> iterator = regionList.iterator();
        while ( iterator.hasNext() ) {
            regions.add( iterator.next() );
        }
        region.setItems(regions);
    }

    private String resultConverter(ButtonType buttonType) {

        if ( buttonType == save) {

            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            if ( newEntry )
                depart = new Depart();
            else
                depart = session.get( Depart.class, this.depart.getId() );
            depart.setName( name.getText() );
            depart.setRegion( region.getValue() );
            session.save( depart );
            session.getTransaction().commit();
            session.close();
            setSelectedRegion( depart.getRegion() );
            setSelectedDepart( depart );
            fillListView();
            return "SAVED";
        } else
            return "CANCELLED";
    }

    private String isLigneValid() {

        String result = "yes";

        if ( name.getText().trim().isEmpty() )
            result = "Le nom du ligne ne doit pas etre vide.";
        else if ( region.getValue() == null )
            result = "Tu doit préciser la region du ligne.";

        return result;
    }
}
