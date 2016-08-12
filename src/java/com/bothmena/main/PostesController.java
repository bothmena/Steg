package com.bothmena.main;

import com.bothmena.entity.Depart;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

@FXMLController(value = "/com.bothmena.main/postes.fxml")
public class PostesController {

    SessionFactory sessionFactory = Main.getSessionFactory();
	@FXML
    JFXTreeTableView<Poste> table;
//	@FXML JFXTreeTableColumn<Poste, Integer> id;
	@FXML JFXTreeTableColumn<Poste, String> name;
	@FXML JFXTreeTableColumn<Poste, String> type;
	@FXML JFXTreeTableColumn<Poste, String> marque;
	@FXML JFXTreeTableColumn<Poste, String> ptr;
	@FXML JFXTreeTableColumn<Poste, String> tele;
	@FXML JFXTreeTableColumn<Poste, String> observation;

	@FXML
    private Label postesCount;
	@FXML
    JFXTextField searchField;
    @FXML
    VBox vBox;

	@PostConstruct
	public void init() throws FlowException, VetoException {

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
        ptr.setCellValueFactory((TreeTableColumn.CellDataFeatures<Poste, String> param) ->{
            if(ptr.validateValue(param))
                return param.getValue().getValue().getPtrProperty();
            else
                return ptr.getComputedValue(param);
        });
        tele.setCellValueFactory((TreeTableColumn.CellDataFeatures<Poste, String> param) ->{
            if(tele.validateValue(param))
                return param.getValue().getValue().getTeleProperty();
            else
                return tele.getComputedValue(param);
        });
        observation.setCellValueFactory((TreeTableColumn.CellDataFeatures<Poste, String> param) ->{
            if(observation.validateValue(param))
                return param.getValue().getValue().getObservationProperty();
            else
                return observation.getComputedValue(param);
        });

		// add editors
		/*name.setCellFactory((TreeTableColumn<Poste, String> param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
		name.setOnEditCommit(
				( CellEditEvent<Poste, String> t ) ->
						t.getTreeTableView().getTreeItem( t.getTreeTablePosition().getRow() ).getValue().setName( t.getNewValue() )
		);
		type.setCellFactory((TreeTableColumn<Poste, String> param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
		type.setOnEditCommit((CellEditEvent<Poste, String> t)-> t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().setType(t.getNewValue()));

        marque.setCellFactory((TreeTableColumn<Poste, String> param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
		marque.setOnEditCommit((CellEditEvent<Poste, String> t)-> t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().setMarque(t.getNewValue()));
*/

		ObservableList<Poste> list = FXCollections.observableArrayList();
        if( Main.getDepartId() > 0 ) {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            Depart depart = session.get(Depart.class, Main.getDepartId());
            Set<Poste> postes = depart.getPostes();
            Iterator<Poste> iterator = postes.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
            session.close();
        }

		table.setRoot(new RecursiveTreeItem<>(list, RecursiveTreeObject::getChildren));
		table.setShowRoot(false);
		table.setEditable(true);
		postesCount.textProperty().bind(Bindings.createStringBinding(()-> "( " + table.getCurrentItemsCount()+" )", table.currentItemsCountProperty()));
		searchField.textProperty().addListener((o, oldVal, newVal)->{
		    String lowerCased = newVal.toLowerCase();
			table.setPredicate(Poste ->
                    Poste.getValue().getName().toLowerCase().contains( lowerCased ) ||
                    Poste.getValue().getType().toLowerCase().contains( lowerCased ) ||
                    Poste.getValue().getMarque().toLowerCase().contains( lowerCased ) ||
                    Poste.getValue().getPtr().toLowerCase().contains( lowerCased ) ||
                    Poste.getValue().getTeleToString().toLowerCase().contains( lowerCased ) ||
                    Poste.getValue().getMarque().toLowerCase().contains( lowerCased )
            );
		});

        vBox.widthProperty().addListener( (observable, oldValue, newValue) -> {
            double width = newValue.doubleValue() - 5;
            name.setPrefWidth( width * 0.2 );
            type.setPrefWidth( width * 0.15 );
            marque.setPrefWidth( width * 0.15 );
            ptr.setPrefWidth( width * 0.1 );
            tele.setPrefWidth( width * 0.1 );
            observation.setPrefWidth( width * 0.3 );
            vBox.setPrefHeight( vBox.getHeight() );
        } );
	}
}
