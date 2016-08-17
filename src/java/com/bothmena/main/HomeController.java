package com.bothmena.main;

import com.bothmena.dialog.DepartDialog;
import com.bothmena.dialog.PosteDialog;
import com.bothmena.dialog.RegionDialog;
import com.bothmena.entity.Depart;
import com.bothmena.entity.Poste;
import com.bothmena.entity.Region;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import de.jensd.fx.fontawesome.AwesomeIcon;
import de.jensd.fx.fontawesome.Icon;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.AnimatedFlowContainer;
import io.datafx.controller.flow.container.ContainerAnimations;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.hibernate.SessionFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by Aymen Ben Othmen on 12/08/16.
 */

@FXMLController(value = "/com.bothmena/main/home.fxml", title = "Material Design Example")
public class HomeController {

    SessionFactory sessionFactory = Main.getSessionFactory();

    @FXMLViewFlowContext
    private ViewFlowContext context;
    private FlowHandler flowHandler;
    private FlowHandler sideMenuFlowHandler;
    @FXML
    private VBox homeRoot;
    private double vOffset;

    @FXML private JFXDialog dialog;
    @FXML private JFXDialogLayout layout;
    @FXML private JFXButton confirm;
    @FXML private JFXButton cancel;

    @FXML private StackPane burgerContainer;
    @FXML private HBox rightBox;
    @FXML private JFXDrawer drawer;
    @FXML private HBox leftBox;
    @FXML private JFXButton config;
    @FXML private JFXButton region;
    @FXML private JFXButton depart;
    @FXML private JFXButton poste;
    @FXML private JFXPopup configPopup;
    @FXML private Label params;
    @FXML private Label save;
    @FXML private Label sync;
    @FXML private Label quit;
    @FXML private JFXPopup regionPopup;
    @FXML private Label newRegion;
    @FXML private Label editRegion;
    @FXML private Label removeRegion;
    @FXML private JFXPopup departPopup;
    @FXML private Label newDepart;
    @FXML private Label editDepart;
    @FXML private Label removeDepart;
    @FXML private JFXPopup postePopup;
    @FXML private Label newPoste;
    @FXML private Label editPoste;
    @FXML private Label removePoste;
    @FXML HBox drawerContainer;

    private static Depart selectedDepart;
    private static Region selectedRegion;

    @PostConstruct
    public void init() throws FlowException, VetoException {

        setLeftToolbar();
        setConfigBtns();

        initPopup();
        initContexFlow();

        initRegion();
        initDepart();
    }

    private void initRegion() {

        newRegion.setOnMouseClicked( event -> {
            regionPopup.close();
            regionDialog(null);
        } );
        editRegion.setOnMouseClicked( event -> {
            regionPopup.close();
            regionDialog(selectedRegion);
        } );
        removeRegion.setOnMouseClicked( event -> {
            regionPopup.close();
            regionDialog(selectedRegion);
        } );
    }

    private void initDepart() {

        newDepart.setOnMouseClicked( event -> {
            departPopup.close();
            departDialog(null);
        } );
        editDepart.setOnMouseClicked( event -> {
            departPopup.close();
            departDialog(selectedDepart);
        } );
        removeDepart.setOnMouseClicked( event -> {
            departPopup.close();
            departDialog(selectedDepart);
        } );
    }

    private void regionDialog(Region region) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.bothmena/dialog/regionDialog.fxml"));
            BorderPane borderPane = loader.load();
            RegionDialog regionDialog = loader.getController();
            String result = regionDialog.showDialog( borderPane, region );
            System.out.println(result);

        } catch (IOException ex) {
            System.out.println("error: " + ex);
        }
    }

    private void departDialog(Depart depart) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.bothmena/dialog/departDialog.fxml"));
            BorderPane borderPane = loader.load();
            DepartDialog controller = loader.getController();
            String result = controller.showDialog( borderPane, depart );
            System.out.println(result);

        } catch (IOException ex) {
            System.out.println("error: " + ex);
        }
    }

    private void posteDialog( Poste poste ) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.bothmena/dialog/posteDialog.fxml"));
            BorderPane borderPane = loader.load();
            PosteDialog controller = loader.getController();
            String result = controller.showDialog( borderPane, selectedDepart, new Poste() );
            System.out.println(result);

        } catch (IOException ex) {
            System.out.println("error: " + ex);
        }
    }

    private void initPopup() {

        vOffset = homeRoot.getHeight();
        homeRoot.getChildren().removeAll(configPopup, regionPopup, departPopup, postePopup);
        homeRoot.heightProperty().addListener(
                (observable, oldValue, newValue) -> vOffset = newValue.doubleValue()
        );
    }

    private void initContexFlow() throws FlowException, VetoException {

        // create the inner flow and content
        context = new ViewFlowContext();
        // set the default controller
        Flow innerFlow = new Flow(PostesController.class);

        flowHandler = innerFlow.createHandler(context);
        context.register( "ContentFlowHandler", flowHandler );
        context.register( "ContentFlow", innerFlow );
        drawer.setContent(flowHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
        context.register("ContentPane", drawer.getContent().get(0));

        // side controller will add links to the content flow
        Flow sideMenuFlow = new Flow(SideMenuController.class);
        sideMenuFlowHandler = sideMenuFlow.createHandler(context);
        drawer.setSidePane( sideMenuFlowHandler.start(
                new AnimatedFlowContainer( Duration.millis(320), ContainerAnimations.SWIPE_LEFT ) )
        );
    }

    private void setConfigBtns() {

        config.setTooltip(new Tooltip("Modifier les parametres du programme."));
        config.setGraphic( new Icon( AwesomeIcon.CARET_DOWN, "18px", null, "config-btn-icon" ));
        configPopup.setPopupContainer(homeRoot);
        configPopup.setSource(config);
        config.setOnMouseClicked((e) -> {
            regionPopup.close();
            departPopup.close();
            postePopup.close();
            configPopup.show(JFXPopup.PopupVPosition.TOP,
                    JFXPopup.PopupHPosition.LEFT, 20D, 204 - vOffset );
        });

        region.setTooltip(new Tooltip("Modifier les parametres de la region selectionnée."));
        region.setGraphic( new Icon( AwesomeIcon.CARET_DOWN, "18px", null, "config-btn-icon" ));
        regionPopup.setPopupContainer(homeRoot);
        regionPopup.setSource(region);
        region.setOnMouseClicked((e) -> {
            postePopup.close();
            departPopup.close();
            configPopup.close();
            regionPopup.show(JFXPopup.PopupVPosition.TOP,
                    JFXPopup.PopupHPosition.LEFT, 20D, 170 - vOffset );
        });

        depart.setTooltip(new Tooltip("Modifier les parametres du départ selectionné."));
        depart.setGraphic( new Icon( AwesomeIcon.CARET_DOWN, "18px", null, "config-btn-icon" ));
        departPopup.setPopupContainer(homeRoot);
        departPopup.setSource(depart);
        depart.setOnMouseClicked((e) -> {
            configPopup.close();
            postePopup.close();
            regionPopup.close();
            departPopup.show(JFXPopup.PopupVPosition.TOP,
                    JFXPopup.PopupHPosition.LEFT, 20D, 170 - vOffset );
        });

        poste.setTooltip(new Tooltip("Modifier les parametres du poste selectionné."));
        poste.setGraphic( new Icon( AwesomeIcon.CARET_DOWN, "18px", null, "config-btn-icon" ));
        postePopup.setPopupContainer(homeRoot);
        postePopup.setSource(poste);
        poste.setOnMouseClicked( (e) -> {
            configPopup.close();
            regionPopup.close();
            departPopup.close();
            postePopup.show(JFXPopup.PopupVPosition.TOP,
                    JFXPopup.PopupHPosition.RIGHT, -10D, 170 - vOffset );//63 + 34*3
        });
    }

    private void setLeftToolbar() {

        Label label = new Label("STEG Suivies Des Departs");
        JFXHamburger hamburger = new JFXHamburger();
        hamburger.setAnimation( new HamburgerBasicCloseTransition() );
        StackPane leftStackPane = new StackPane();
        leftStackPane.setPadding( new Insets(13d) );
        leftStackPane.getChildren().add( hamburger );
        JFXRippler leftRippler = new JFXRippler( leftStackPane );
        leftRippler.setMargin(leftStackPane, new Insets( 2d ));
        leftBox.getChildren().addAll( leftRippler, label );
        // Hamburger & Drawer events handling.
        leftStackPane.setOnMouseClicked( event -> {
            hamburger.getAnimation().setRate( hamburger.getAnimation().getRate() * -1 );
            hamburger.getAnimation().play();
        } );

        drawer.setOnDrawerOpening((e) -> {
            hamburger.getAnimation().setRate(1);
            hamburger.getAnimation().play();
        });
        drawer.setOnDrawerClosing((e) -> {
            hamburger.getAnimation().setRate(-1);
            hamburger.getAnimation().play();
        });
        leftStackPane.setOnMouseClicked((e)->{
            if ( drawer.isHidden() || drawer.isHidding() )
                drawer.open();
            else
                drawer.close();
        });
    }

    public static void setSelectedDepart(Depart depart) {
        selectedDepart = depart;
    }

    public static void setSelectedRegion(Region region) {
        selectedRegion = region;
    }
}
