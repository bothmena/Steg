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
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static com.bothmena.main.MainController.*;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@FXMLController(value = "/com.bothmena/main/sideMenu.fxml", title = "Material Design Example")
public class SideMenuController {

    private static SessionFactory sessionFactory = Main.getSessionFactory();
	@FXMLViewFlowContext
    private ViewFlowContext context;
    private Flow contentFlow;
    private static FlowHandler contentFlowHandler;
    @FXML private static StackPane stackPane;

	@PostConstruct
	public void init() throws FlowException, VetoException {

        contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        fillListView();
	}

    private static JFXListView<Label> getRegionList() {

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        JFXListView<Label> sideList = new JFXListView<>();
        sideList.getStyleClass().add("custom-jfx-list-view");
        List<Region> regionList = session.createQuery("from com.bothmena.entity.Region").list();

        Label first = getLabel( AwesomeIcon.COMPASS, "Liste Des Regions" );
        first.setWrapText(true);
        sideList.getItems().add( first );
        first.setOnMouseClicked( event -> setRDP(null) );
        for ( Region region : regionList ) {

            Label label = getLabel( AwesomeIcon.MAP_MARKER, region.getName() );
            label.setOnMouseClicked( event -> setRDP(region) );
            sideList.getItems().add( label );
        }
        session.close();
        return sideList;
    }

    private static JFXListView<Label> getDepartList() {

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        JFXListView<Label> sideList = new JFXListView<>();
        sideList.getStyleClass().add("custom-jfx-list-view");

        Region region = session.get( Region.class, getSelectedRegion().getId() );
        Set<Depart> departList = region.getDeparts();

        Label first = getLabel( AwesomeIcon.ANGLE_DOUBLE_LEFT, region.getName() );
        first.setOnMouseClicked( event -> {
            setSelectedRegion( null );
            HomeController.setSelectedRegion( null );
        } );
        first.setWrapText(false);
        sideList.getItems().add( first );
        for ( Depart depart : departList ) {

            Label label = getLabel( AwesomeIcon.CODE_FORK, depart.getName() );
            label.setOnMouseClicked( event -> setSelectedDepart( depart ) );
            sideList.getItems().add( label );
        }
        session.close();
        return sideList;
    }

    private static Label getLabel (AwesomeIcon icon, String name ) {

        Label label = new Label( name );
        label.setGraphic( new Icon( icon, "1.6em", ";", "custom-jfx-list-view-icon" ) );
        return label;
    }

    public static void fillListView () {

        if ( getSelectedRegion() == null ) {
            stackPane.getChildren().clear();
            stackPane.getChildren().add( getRegionList() );
        }
        else {
            stackPane.getChildren().clear();
            stackPane.getChildren().add( getDepartList() );
        }
    }

    public static void updateContent() throws VetoException, FlowException {
        contentFlowHandler.navigateTo( PostesController.class );
    }

    public static void setRDP(Region region ) {

        setSelectedRegion( region );
        setSelectedDepart( null );
        fillListView();
    }
}
