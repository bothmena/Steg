package com.bothmena.main;

import com.bothmena.entity.Poste;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.util.VetoException;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;
import java.util.Random;

@FXMLController(value = "/com.bothmena.main/postes.fxml")
public class PostesController {

	@FXML
    JFXTreeTableView<Poste> table;
//	@FXML JFXTreeTableColumn<Poste, Integer> id;
	@FXML
JFXTreeTableColumn<Poste, String> name;
	@FXML
    JFXTreeTableColumn<Poste, String> type;
	@FXML
    JFXTreeTableColumn<Poste, String> marque;
/*	@FXML JFXTreeTableColumn<Poste, String> ptr;
	@FXML JFXTreeTableColumn<Poste, String> tele;
	@FXML JFXTreeTableColumn<Poste, String> observation;*/

	@FXML
    Label postesCount;
	@FXML
    JFXTextField searchField;
    @FXML
    VBox vBox;

	@PostConstruct
	public void init() throws FlowException, VetoException {

		String[] names = { "Morley", "Scott", "Kruger", "Lain",
				"Kennedy", "Gawron", "Han", "Hall", "Aydogdu", "Grace",
				"Spiers", "Perera", "Smith", "Connoly",
				"Sokolowski", "Chaow", "James", "June" };

        String[] types = { "Standard", "Smart", "Automatique", "Manuel" };

        String[] marques = { "Motorella", "Sony", "Samsung", "LapTop", "Downside", "BlownUp", "Hatchu" };
		Random random = new Random();
		
		
		name.setCellValueFactory((TreeTableColumn.CellDataFeatures<Poste, String> param) ->{
			if(name.validateValue(param))
			    return param.getValue().getValue().getNameProperty();
			else
			    return name.getComputedValue(param);
		});
		type.setCellValueFactory((TreeTableColumn.CellDataFeatures<Poste, String> param) ->{
			if(type.validateValue(param))
			    return param.getValue().getValue().getTypeProperty();
			else
			    return type.getComputedValue(param);
		});
		marque.setCellValueFactory((TreeTableColumn.CellDataFeatures<Poste, String> param) ->{
			if(marque.validateValue(param))
			    return param.getValue().getValue().getMarqueProperty();
			else
			    return marque.getComputedValue(param);
		});
		// add editors
		name.setCellFactory((TreeTableColumn<Poste, String> param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
		name.setOnEditCommit(
				( CellEditEvent<Poste, String> t ) ->
						t.getTreeTableView().getTreeItem( t.getTreeTablePosition().getRow() ).getValue().setName( t.getNewValue() )
		);
		type.setCellFactory((TreeTableColumn<Poste, String> param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
		type.setOnEditCommit((CellEditEvent<Poste, String> t)-> t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().setType(t.getNewValue()));

        marque.setCellFactory((TreeTableColumn<Poste, String> param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
		marque.setOnEditCommit((CellEditEvent<Poste, String> t)-> t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().setMarque(t.getNewValue()));


		ObservableList<Poste> list = FXCollections.observableArrayList();
		for (int i = 0; i < 200; i++) {
            list.add(
                new Poste(
                    names[random.nextInt(names.length)],
                    types[random.nextInt(types.length)],
                    marques[random.nextInt(marques.length)]
                )
            );
        }

		table.setRoot(new RecursiveTreeItem<>(list, RecursiveTreeObject::getChildren));
		table.setShowRoot(false);
		table.setEditable(true);
		postesCount.textProperty().bind(Bindings.createStringBinding(()-> "( " + table.getCurrentItemsCount()+" )", table.currentItemsCountProperty()));
		searchField.textProperty().addListener((o, oldVal, newVal)->{
			table.setPredicate(Poste ->
                    Poste.getValue().getName().contains( newVal ) ||
                    Poste.getValue().getType().contains( newVal ) ||
                    Poste.getValue().getMarque().contains( newVal )
            );
		});

        vBox.widthProperty().addListener( (observable, oldValue, newValue) -> {
            name.setPrefWidth( ( newValue.doubleValue() - 18 ) * 0.4 );
            type.setPrefWidth( ( newValue.doubleValue() - 18 ) * 0.3 );
            marque.setPrefWidth( ( newValue.doubleValue() - 18 ) * 0.3 );
            vBox.setPrefHeight( vBox.getHeight() );
        } );
	}
}
