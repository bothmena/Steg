package com.bothmena.main;

import com.bothmena.entity.Depart;
import com.bothmena.entity.Region;
import com.jfoenix.controls.JFXListView;
import de.jensd.fx.fontawesome.AwesomeIcon;
import de.jensd.fx.fontawesome.Icon;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.FlowActionHandler;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

//import demos.gui.uicomponents.*;

@FXMLController(value = "/com.bothmena.main/sideMenu.fxml", title = "Material Design Example")
public class SideMenuController {

    SessionFactory sessionFactory = Main.getSessionFactory();
	@FXMLViewFlowContext
    private ViewFlowContext context;
    @ActionHandler
    private FlowActionHandler actionHandler;
    private Flow contentFlow;
    private FlowHandler contentFlowHandler;
    @FXML
    private StackPane stackPane;

	@PostConstruct
	public void init() throws FlowException, VetoException {

        contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");

        fillListView( 0 );
	}

	private void fillListView ( int regionId ) {

        if ( regionId == 0 ) {
            stackPane.getChildren().clear();
            stackPane.getChildren().add( getRegionList() );
        }
        else if ( regionId > 0 ) {
            stackPane.getChildren().clear();
            stackPane.getChildren().add( getDepartList( regionId ) );
        }
    }

    public JFXListView<Label> getRegionList () {

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        JFXListView<Label> sideList = new JFXListView<>();
        sideList.getStyleClass().add("custom-jfx-list-view");
        List<Region> regionList = session.createQuery("from com.bothmena.entity.Region").list();

        Label first = getLabel( AwesomeIcon.COMPASS, "Liste Des Regions" );
        first.setWrapText(true);
        sideList.getItems().add( first );
        for ( Region region : regionList ) {

            Label label = getLabel( AwesomeIcon.MAP_MARKER, region.getName() );
            label.setOnMouseClicked( event -> fillListView( region.getId() ) );
            sideList.getItems().add( label );
        }
        session.close();
        return sideList;
    }

    public JFXListView<Label> getDepartList (int regionId ) {

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        JFXListView<Label> sideList = new JFXListView<>();
        sideList.getStyleClass().add("custom-jfx-list-view");

        Region region = session.get( Region.class, regionId );
        Set<Depart> departList = region.getDeparts();

        Label first = getLabel( AwesomeIcon.ANGLE_DOUBLE_LEFT, region.getName() );
        first.setOnMouseClicked( event -> fillListView( 0 ) );
        first.setWrapText(false);
        sideList.getItems().add( first );
        for ( Depart depart : departList ) {

            Label label = getLabel( AwesomeIcon.CODE_FORK, depart.getName() );
            label.setOnMouseClicked( event -> departListener( depart ) );
            sideList.getItems().add( label );
        }
        session.close();
        return sideList;
    }

    private Label getLabel (AwesomeIcon icon, String name ) {

        Label label = new Label( name );
        label.setGraphic( new Icon( icon, "1.6em", ";", "custom-jfx-list-view-icon" ) );
        return label;
    }

    public void departListener( Depart depart ) {

        try {
            Main.setDepartId( depart.getId() );
            contentFlowHandler.navigateTo( PostesController.class );
        } catch (VetoException | FlowException e) {
            e.printStackTrace();
        }
    }
}
