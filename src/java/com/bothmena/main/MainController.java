package com.bothmena.main;

import com.bothmena.dialog.DialogLauncher;
import com.bothmena.entity.Depart;
import com.bothmena.entity.Poste;
import com.bothmena.entity.Region;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;
import com.jfoenix.controls.JFXRippler;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.ContainerAnimations;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import com.bothmena.datafx.AnimatedFlowContainer;
import javax.annotation.PostConstruct;

import java.util.Objects;

import static com.bothmena.dialog.DialogLauncher.*;
import static com.bothmena.dialog.DialogLauncher.region;
import static com.bothmena.dialog.DialogLauncher.removeRegion;
import static com.bothmena.main.SideMenuController.fillListView;
import static com.bothmena.main.SideMenuController.updateContent;

@FXMLController(value = "/com.bothmena/main/main.fxml", title = "Material Design Example")
public class MainController {

	@FXML private StackPane root;
	
	@FXML private StackPane titleBurgerContainer;
	@FXML private JFXHamburger titleBurger;
	
	@FXML private StackPane configBurger;
	@FXML private JFXRippler configRippler;
    @FXML private JFXPopup configPopup;
    @FXML private Label params;
    @FXML private Label save;
    @FXML private Label sync;
    @FXML private Label exit;

    @FXML private StackPane regionBurger;
    @FXML private JFXRippler regionRippler;
    @FXML private JFXPopup regionPopup;
    @FXML private Label newRegion;
    @FXML private Label editRegion;
    @FXML private Label removeRegion;

    @FXML private StackPane departBurger;
    @FXML private JFXRippler departRippler;
    @FXML private JFXPopup departPopup;
    @FXML private Label newDepart;
    @FXML private Label editDepart;
    @FXML private Label removeDepart;

    @FXML private StackPane posteBurger;
    @FXML private JFXRippler posteRippler;
    @FXML private JFXPopup postePopup;
    @FXML private Label newPoste;
    @FXML private Label editPoste;
    @FXML private Label removePoste;
	
	@FXML private static JFXDrawer drawer;

	@FXMLViewFlowContext
    private ViewFlowContext context;
	private FlowHandler flowHandler;
	private FlowHandler sideMenuFlowHandler;

    @FXML private static Label regionName;
    @FXML private static Label departName;
    @FXML private static Label posteName;
    private static Region selectedRegion = null;
    private static Depart selectedDepart = null;
    private static Poste selectedPoste = null;

    @PostConstruct
	public void init() throws FlowException, VetoException {

	    initDrawer();
        initPopup();
        initConfig();
        initRegion();
        initDepart();
        initPoste();
        initContext();
	}

    private void initConfig() {
        exit.setOnMouseClicked((e) -> {
            Main.closeStage();
        });
    }

    private void initRegion() {

        newRegion.setOnMouseClicked( event -> {
            regionPopup.close();
            region(null);
        } );
        editRegion.setOnMouseClicked( event -> {
            regionPopup.close();
            if( selectedRegion != null )
                region(selectedRegion);
            else
                DialogLauncher.notify("Please select a region first.");
        } );
        removeRegion.setOnMouseClicked( event -> {
            regionPopup.close();
            if( selectedRegion != null )
                removeRegion(selectedRegion);
            else
                DialogLauncher.notify("Please select a region first.");
        } );
    }

    private void initDepart() {

        newDepart.setOnMouseClicked( event -> {
            departPopup.close();
            depart(null);
        } );
        editDepart.setOnMouseClicked( event -> {
            departPopup.close();
            if( selectedDepart != null )
                depart(selectedDepart);
            else
                DialogLauncher.notify("Please select a depart first.");
        } );
        removeDepart.setOnMouseClicked( event -> {
            departPopup.close();
            if( selectedDepart != null )
                removeDepart(selectedDepart);
            else
                DialogLauncher.notify("Please select a depart first.");
        } );
    }

    private void initPoste() {
        newPoste.setOnMouseClicked( event -> {
            postePopup.close();
            poste( null );
        } );
        editPoste.setOnMouseClicked( event -> {
            postePopup.close();
            if( selectedPoste != null )
                poste( selectedPoste );
            else
                DialogLauncher.notify("Please select a poste first.");
        } );
        removePoste.setOnMouseClicked( event -> {
            postePopup.close();
            if( selectedPoste != null )
                removePoste(selectedPoste);
            else
                DialogLauncher.notify("Please select a poste first.");
        } );
    }

	private void initPopup() {

        configPopup.setPopupContainer(root);
        configPopup.setSource(configRippler);
        root.getChildren().remove(configPopup);
        configBurger.setOnMouseClicked((e) -> {
            configPopup.show(PopupVPosition.TOP, PopupHPosition.LEFT, 0, 45);
        });

        regionPopup.setPopupContainer(root);
        regionPopup.setSource(regionRippler);
        root.getChildren().remove(regionPopup);
        regionBurger.setOnMouseClicked((e) -> {
            regionPopup.show(PopupVPosition.TOP, PopupHPosition.LEFT, 0, 45);
        });

        departPopup.setPopupContainer(root);
        departPopup.setSource(departRippler);
        root.getChildren().remove(departPopup);
        departBurger.setOnMouseClicked((e) -> {
            departPopup.show(PopupVPosition.TOP, PopupHPosition.LEFT, 0, 45);
        });

        postePopup.setPopupContainer(root);
        postePopup.setSource(posteRippler);
        root.getChildren().remove(postePopup);
        posteBurger.setOnMouseClicked((e) -> {
            postePopup.show(PopupVPosition.TOP, PopupHPosition.RIGHT, 0, 45);
        });
    }

    private void initContext() throws FlowException, VetoException {

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
        drawer.setSidePane(sideMenuFlowHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
    }

    private void initDrawer() {

        drawer.setOnDrawerOpening((e) -> {
            titleBurger.getAnimation().setRate(1);
            titleBurger.getAnimation().play();
        });
        drawer.setOnDrawerClosing((e) -> {
            titleBurger.getAnimation().setRate(-1);
            titleBurger.getAnimation().play();
        });
        titleBurgerContainer.setOnMouseClicked((e)->{
            if (drawer.isHidden() || drawer.isHidding())
                drawer.open();
            else
                drawer.close();
        });
    }

    public static void closeDrawer() {

        if( drawer.isShown() || drawer.isShowing() ) {
            drawer.close();
        }
    }

    public static Region getSelectedRegion() {
        return selectedRegion;
    }

    public static void setSelectedRegion (Region selectedRegion) {
        MainController.selectedRegion = selectedRegion;
        setRegionName();
    }

    public static Depart getSelectedDepart() {
        return selectedDepart;
    }

    public static void setSelectedDepart(Depart selectedDepart) {

        try {
            if( !Objects.equals( MainController.selectedDepart, selectedDepart )) {
                MainController.selectedDepart = selectedDepart;
                setDepartName();
                setSelectedPoste( null );
                closeDrawer();
                updateContent();
            }
        } catch (VetoException | FlowException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public static Poste getSelectedPoste() {
        return selectedPoste;
    }

    public static void setSelectedPoste(Poste selectedPoste) {
        MainController.selectedPoste = selectedPoste;
        setPosteName();
    }

    public static void setRegionName() {
        regionName.setText( Objects.
                equals(selectedRegion, null) ? "Aucune Selectionnée" : selectedRegion.getName() );
    }

    public static void setDepartName() {
        departName.setText( Objects.
                equals(selectedDepart, null) ? "Aucun Selectionné" : selectedDepart.getName() );
    }

    public static void setPosteName() {
        posteName.setText( Objects.
                equals(selectedPoste, null) ? "Aucun Selectionné" : selectedPoste.getName() );
    }
}
