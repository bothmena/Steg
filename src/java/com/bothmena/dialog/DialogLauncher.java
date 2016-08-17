package com.bothmena.dialog;

import com.bothmena.entity.Depart;
import com.bothmena.entity.Poste;
import com.bothmena.entity.Region;
import com.bothmena.main.Main;
import com.bothmena.main.MainController;
import com.bothmena.main.SideMenuController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.Objects;

import static com.bothmena.main.MainController.*;
import static com.bothmena.main.SideMenuController.fillListView;
import static com.bothmena.main.SideMenuController.updateContent;

/**
 * Created by Aymen Ben Othmen on 14/08/16.
 */

public class DialogLauncher {

    private static SessionFactory sessionFactory = Main.getSessionFactory();

    public static void region(Region region) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    DialogLauncher.class.getResource("/com.bothmena/dialog/regionDialog.fxml")
            );
            BorderPane borderPane = loader.load();
            RegionDialog regionDialog = loader.getController();
            regionDialog.showDialog( borderPane, region );

        } catch (IOException ex) {
            System.out.println("error: " + ex);
        }
    }

    public static void depart(Depart depart) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    DialogLauncher.class.getResource("/com.bothmena/dialog/departDialog.fxml")
            );
            BorderPane borderPane = loader.load();
            DepartDialog controller = loader.getController();
            controller.showDialog( borderPane, depart );

        } catch (IOException ex) {
            System.out.println("error: " + ex);
        }
    }

    public static void poste( Poste poste ) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    DialogLauncher.class.getResource("/com.bothmena/dialog/posteDialog.fxml")
            );
            BorderPane borderPane = loader.load();
            PosteDialog controller = loader.getController();
            controller.showDialog( borderPane, getSelectedDepart(), poste );

        } catch (IOException ex) {
            System.out.println("error: " + ex);
        }
    }

    public static String removeRegion( Region region ) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    DialogLauncher.class.getResource("/com.bothmena/dialog/confirmation.fxml")
            );
            BorderPane borderPane = loader.load();
            Confirmation controller = loader.getController();
            String title = "Confirmation de suppression d'une region";
            String message = "Etes vous sur de supprimer la region " + region.getName();
            String result = controller.showDialog( borderPane, title, message );

            if (Objects.equals(result, "CONFIRMED")) {
                Session session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                region = session.get(Region.class, region.getId());
                region.removeDeparts(session);
                session.remove(region);
                session.getTransaction().commit();
                session.close();
                setSelectedRegion(null);
                setSelectedDepart(null);
                fillListView();
                return "REMOVED";
            }

        } catch (IOException ex) {
            System.out.println("error: " + ex);
        }
        return "REMOVE_CANCELLED";
    }

    public static String removeDepart( Depart depart ) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    DialogLauncher.class.getResource("/com.bothmena/dialog/confirmation.fxml"));
            BorderPane borderPane = loader.load();
            Confirmation controller = loader.getController();
            String title = "Confirmation de suppression d'un depart";
            String message = String.format( "Etes vous sur de supprimer le depart '%s' de %s", depart.getName(), depart.getRegion().getName() );
            String result = controller.showDialog( borderPane, title, message );

            if (Objects.equals(result, "CONFIRMED")) {

                Session session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                depart = session.get(Depart.class, depart.getId());
                depart.removePostes(session);
                session.remove(depart);
                session.getTransaction().commit();
                session.close();
                setSelectedDepart(null);
                fillListView();
                return "REMOVED";
            }

        } catch (IOException ex) {
            System.out.println("error: " + ex);
        }
        return "REMOVE_CANCELLED";
    }

    public static String removePoste( Poste poste ) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    DialogLauncher.class.getResource("/com.bothmena/dialog/confirmation.fxml")
            );
            BorderPane borderPane = loader.load();
            Confirmation controller = loader.getController();
            String title = "Confirmation de suppression d'un poste";
            String message = String.format("Etes vous sur de supprimer le poste '%s' de %s", poste.getName(), poste.getDepart().getName());
            String result = controller.showDialog(borderPane, title, message);
            if (Objects.equals(result, "CONFIRMED")) {
                Session session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                poste = session.get(Poste.class, poste.getId());
                session.remove(poste);
                session.getTransaction().commit();
                session.close();
                try {
                    updateContent();
                } catch (VetoException | FlowException e) {
                    System.out.println(e);
                }
                return "REMOVED";
            }

        } catch (IOException ex) {
            System.out.println("error: " + ex);
        }
        return "REMOVE_CANCELLED";
    }

    public static void notify( String notification ) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    DialogLauncher.class.getResource("/com.bothmena/dialog/notification.fxml")
            );
            BorderPane borderPane = loader.load();
            Notification controller = loader.getController();
            controller.showDialog( borderPane, notification );

        } catch (IOException ex) {
            System.out.println("error: " + ex);
        }
    }
}
