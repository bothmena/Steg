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
import javafx.scene.layout.VBox;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.annotation.PostConstruct;
import java.util.Set;

import static com.bothmena.main.MainController.getSelectedDepart;
import static com.bothmena.main.MainController.setPosteName;
import static com.bothmena.main.MainController.setSelectedPoste;

@FXMLController(value = "/com.bothmena/main/postes.fxml")
public class PostesController {

    private SessionFactory sessionFactory = Main.getSessionFactory();
	@FXML private JFXTreeTableView<Poste> table;
	@FXML private JFXTreeTableColumn<Poste, String> name;
	@FXML private JFXTreeTableColumn<Poste, String> type;
	@FXML private JFXTreeTableColumn<Poste, String> marque;
	@FXML private JFXTreeTableColumn<Poste, String> ptr;
	@FXML private JFXTreeTableColumn<Poste, String> tele;
	@FXML private JFXTreeTableColumn<Poste, String> observation;

	@FXML private Label postesCount;
	@FXML private JFXTextField searchField;
    @FXML private VBox vBox;

	@PostConstruct
	public void init() throws FlowException, VetoException {

		setcellFactories();
		setTable();
		postesCount.textProperty().bind(Bindings.createStringBinding(()-> "( " + table.getCurrentItemsCount()+" )", table.currentItemsCountProperty()));
		setSearchFilter();
		vBoxListener();
	}

	private void setTable() {

		ObservableList<Poste> list = FXCollections.observableArrayList();
		Depart depart = getSelectedDepart();
		if( depart != null ) {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			depart = session.get(Depart.class, getSelectedDepart().getId() );
			Set<Poste> postes = depart.getPostes();
			for (Poste poste : postes) {
				list.add(poste);
			}
			session.close();
		}

		table.setRoot(new RecursiveTreeItem<>(list, RecursiveTreeObject::getChildren));
		table.setShowRoot(false);
		table.setEditable(false);
		table.setOnMouseClicked( event -> {
			try {
				setSelectedPoste( table.getSelectionModel().getSelectedItem().getValue() );
			}catch (NullPointerException e) {}
		});
	}

	private void setcellFactories() {

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
	}

	private void setSearchFilter() {

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
	}

	private void vBoxListener() {

		vBox.widthProperty().addListener( (observable, oldValue, newValue) -> {
			
			double totalWidth = newValue.doubleValue() - 5;
			
			name.         setPrefWidth( totalWidth * 0.2 );
			type.         setPrefWidth( totalWidth * 0.15 );
			marque.       setPrefWidth( totalWidth * 0.15 );
			ptr.          setPrefWidth( totalWidth * 0.15 );
			tele.         setPrefWidth( totalWidth * 0.1 );
			observation.  setPrefWidth( totalWidth * 0.25 );
		} );
	}

    /*private void addEditors() {

		name.setCellFactory((TreeTableColumn<Poste, String> param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
		name.setOnEditCommit(
				( TreeTableColumn.CellEditEvent<Poste, String> t ) ->
						t.getTreeTableView().getTreeItem( t.getTreeTablePosition().getRow() ).getValue().setName( t.getNewValue() )
		);
		type.setCellFactory((TreeTableColumn<Poste, String> param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
		type.setOnEditCommit((TreeTableColumn.CellEditEvent<Poste, String> t)-> t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().setType(t.getNewValue()));

        marque.setCellFactory((TreeTableColumn<Poste, String> param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
		marque.setOnEditCommit((TreeTableColumn.CellEditEvent<Poste, String> t)-> t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().setMarque(t.getNewValue()));
    }*/
}
