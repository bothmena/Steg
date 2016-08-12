package com.bothmena.entity;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;

/**
 * Created by Aymen Ben Othmen on 19/07/16.
 */

@Entity
@Table(name = "poste")
public class Poste extends RecursiveTreeObject<Poste> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "marque")
    private String marque;

    @Column(name = "ptr")
    private String ptr;

    @Column(name = "tele")
    private boolean tele;

    @Column(name = "observation")
    private String observation;

    @ManyToOne
    @JoinColumn(name="depart_id")
    private Depart depart;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="prev_poste_id")
    private Poste prevPoste;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="next_poste_id")
    private Poste nextPoste;

    public Poste() {
        this("", "", "", false);
    }

    public Poste(String name, String type, String ptr) {
        this(name, type, ptr, false);
    }

    public Poste(String name, String type, String ptr, boolean tele ) {

        this.name = name;
        this.type = type;
        this.marque = ptr;//this.ptr = ptr;
        this.tele = tele;
    }

    public StringProperty getNameProperty() {
        return new SimpleStringProperty(this.name);
    }

    public StringProperty getTypeProperty() {
        return new SimpleStringProperty(this.type);
    }

    public StringProperty getMarqueProperty() {
        return new SimpleStringProperty(this.marque);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getPtr() {
        return ptr;
    }

    public void setPtr(String ptr) {
        this.ptr = ptr;
    }

    public boolean getTele() {
        return tele;
    }

    public void setTele(boolean tele) {
        this.tele = tele;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Depart getDepart() {
        return depart;
    }

    public void setDepart(Depart depart) {
        this.depart = depart;
    }

    public Poste getPrevPoste() {
        return prevPoste;
    }

    public void setPrevPoste(Poste prevPoste) {
        this.prevPoste = prevPoste;
    }

    public Poste getNextPoste() {
        return nextPoste;
    }

    public void setNextPoste(Poste nextPoste) {
        this.nextPoste = nextPoste;
    }
}
