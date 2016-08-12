package com.bothmena.main;

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
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javax.annotation.PostConstruct;

/**
 * Created by Aymen Ben Othmen on 12/08/16.
 */

@FXMLController(value = "/com.bothmena.main/home.fxml", title = "Material Design Example")
public class HomeController {

    @FXMLViewFlowContext
    private ViewFlowContext context;
    private FlowHandler flowHandler;
    private FlowHandler sideMenuFlowHandler;
    @FXML
    private VBox homeRoot;
    private double vOffset;

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

    @PostConstruct
    public void init() throws FlowException, VetoException {

        setLeftToolbar();
        setConfigBtns();
        vOffset = homeRoot.getHeight();
        homeRoot.getChildren().removeAll(configPopup, regionPopup, departPopup, postePopup);
        homeRoot.heightProperty().addListener(
                (observable, oldValue, newValue) -> vOffset = newValue.doubleValue()
        );

        JFXDialog dialog = new JFXDialog();

        // create the inner flow and content
        context = new ViewFlowContext();
        // set the default controller
        Flow innerFlow = new Flow(PostesController.class);

        flowHandler = innerFlow.createHandler(context);
        context.register("ContentFlowHandler", flowHandler);
        context.register("ContentFlow", innerFlow);
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
            configPopup.show(JFXPopup.PopupVPosition.TOP,
                    JFXPopup.PopupHPosition.LEFT, 20D, 204 - vOffset );
        });

        region.setTooltip(new Tooltip("Modifier les parametres de la region selectionnée."));
        region.setGraphic( new Icon( AwesomeIcon.CARET_DOWN, "18px", null, "config-btn-icon" ));
        regionPopup.setPopupContainer(homeRoot);
        regionPopup.setSource(region);
        region.setOnMouseClicked((e) -> {
            regionPopup.show(JFXPopup.PopupVPosition.TOP,
                    JFXPopup.PopupHPosition.LEFT, 20D, 170 - vOffset );
        });

        depart.setTooltip(new Tooltip("Modifier les parametres du départ selectionné."));
        depart.setGraphic( new Icon( AwesomeIcon.CARET_DOWN, "18px", null, "config-btn-icon" ));
        departPopup.setPopupContainer(homeRoot);
        departPopup.setSource(depart);
        depart.setOnMouseClicked((e) -> {
            departPopup.show(JFXPopup.PopupVPosition.TOP,
                    JFXPopup.PopupHPosition.LEFT, 20D, 170 - vOffset );
        });

        poste.setTooltip(new Tooltip("Modifier les parametres du poste selectionné."));
        poste.setGraphic( new Icon( AwesomeIcon.CARET_DOWN, "18px", null, "config-btn-icon" ));
        postePopup.setPopupContainer(homeRoot);
        postePopup.setSource(poste);
        poste.setOnMouseClicked( (e) -> {
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

}
