package com.bothmena.main;

import com.bothmena.entity.Depart;
import com.bothmena.entity.Region;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyphLoader;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by Aymen Ben Othmen on 12/08/16.
 */

public class Main extends Application {

    @FXMLViewFlowContext
    private ViewFlowContext flowContext;
    private static final SessionFactory sessionFactory =
            new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();
    private static int departId = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        new Thread(()->{
            try {
                SVGGlyphLoader.loadGlyphsFont(Main.class.getResourceAsStream("/fonts/icomoon.svg"),"icomoon.svg");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }).start();

        Flow flow = new Flow(HomeController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", primaryStage);
        flow.createHandler(flowContext).start(container);

        JFXDecorator decorator = new JFXDecorator(primaryStage, container.getView(), false, true, true);
        decorator.setOnCloseButtonAction( () -> {
            try {
                sessionFactory.close();
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            primaryStage.close();
        } );

        Scene scene = new Scene(decorator, 1000, 600);
        scene.getStylesheets().add(Main.class.getResource("/assets/css/main.min.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("/assets/images/icon.jpg"));
        primaryStage.setTitle("STEG Departs & Postes");
        primaryStage.show();

    }

    public static int getDepartId() {
        return departId;
    }

    public static void setDepartId(int departId) {
        Main.departId = departId;
    }

    public static SessionFactory getSessionFactory() {

        return sessionFactory;
    }
}
