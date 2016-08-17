package com.bothmena.dialog;

import com.bothmena.main.Main;
import com.bothmena.entity.Region;
import com.bothmena.main.MainController;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.Optional;

import static com.bothmena.main.SideMenuController.fillListView;

/**
 * Created by Aymen Ben Othmen on 30/07/16.
 */

public class RegionDialog {

    private static SessionFactory sessionFactory = Main.getSessionFactory();

    @FXML private JFXTextField name;
    @FXML private Label logLabel;
    private Region region;
    private boolean newRegion = false;

    private Dialog<String> dialog = new Dialog<>();
    private ButtonType save = new ButtonType("Sauvegarder", ButtonBar.ButtonData.APPLY);
    private ButtonType cancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

    public String showDialog( BorderPane borderPane, Region region ) {

        DialogSetter.setDialog( dialog, borderPane );
        DialogSetter.setButtons( dialog, save, cancel );
        if( region == null ) {
            newRegion = true;
        } else {
            this.region = region;
            this.name.setText( this.region.getName() );
        }

        final Button submit = (Button) dialog.getDialogPane().lookupButton(save);
        submit.addEventFilter(ActionEvent.ACTION, event -> {
            if ( name.getText().trim().isEmpty() ){
                event.consume();
                logLabel.setText( "Le nom de la region ne doit pas etre vide." );
            }
        });

        dialog.setResultConverter(this::resultConverter);
        Optional<String> result = dialog.showAndWait();

        if ( result.isPresent() )
            return result.get();
        else
            return null;
    }

    private String resultConverter(ButtonType buttonType) {

        if ( buttonType == save) {

            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            if( newRegion )
                region = new Region();
            else
                region = session.get( Region.class, this.region.getId() );
            region.setName( name.getText() );
            session.save( region );
            session.getTransaction().commit();
            session.close();
            MainController.setSelectedRegion( region );
            fillListView();
            return "SAVED";
        }else
            return "CANCELLED";
    }
}
