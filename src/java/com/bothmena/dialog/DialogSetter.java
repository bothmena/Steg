package com.bothmena.dialog;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by Aymen Ben Othmen on 14/08/16.
 */

class DialogSetter {

    static void setButtons(Dialog<String> dialog, ButtonType... buttonTypes) {

        dialog.getDialogPane().getButtonTypes().addAll( buttonTypes );
        ButtonBar buttonBar = (ButtonBar) dialog.getDialogPane().getChildren().get(2);
        for (Node node : buttonBar.getButtons()) {
            Button button = (Button) node;
            switch (button.getText()) {
                case "Sauvegarder":
                    button.getStyleClass().add("btn-primary");
                    break;
                case "Annuler":
                    button.getStyleClass().add("btn-mute");
                    break;
                case "Supprimer":
                    button.getStyleClass().add("btn-danger");
                    break;
                case "OK":
                    button.getStyleClass().add("btn-primary");
                    break;
            }
        }
    }

    static void setDialog(Dialog<String> dialog, BorderPane borderPane) {

        dialog.getDialogPane().setContent(borderPane);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialog.getDialogPane().setStyle("-fx-background-color: #FFFFFF");
        dialog.getDialogPane().getStylesheets().
                add( PosteDialog.class.getResource( "/assets/css/main.min.css" ).toExternalForm() );
        stage.getIcons().add(new Image("/assets/images/icon.jpg"));
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setResizable(false);
    }

}
