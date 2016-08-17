package com.bothmena.dialog;

import com.bothmena.entity.Region;
import com.bothmena.main.Main;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

/**
 * Created by Aymen Ben Othmen on 30/07/16.
 */

public class Confirmation {

    private static ButtonType remove = new ButtonType("Supprimer", ButtonBar.ButtonData.APPLY);
    private static ButtonType cancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
    @FXML private Label title;
    @FXML private Label message;
    private Dialog<String> dialog = new Dialog<>();

    public String showDialog( BorderPane borderPane, String title, String message ) {

        this.title.setText( title );
        this.message.setText( message );
        DialogSetter.setDialog( dialog, borderPane );
        DialogSetter.setButtons( dialog, remove, cancel );

        dialog.setResultConverter(this::resultConverter);
        Optional<String> result = dialog.showAndWait();

        if ( result.isPresent() )
            return result.get();
        else
            return null;
    }

    private String resultConverter(ButtonType buttonType) {

        return buttonType == remove ? "CONFIRMED" : "CANCELLED";
    }
}
