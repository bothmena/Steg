package com.bothmena.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Aymen Ben Othmen on 19/07/16.
 */

@Entity
@Table(name = "depart")
public class Depart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name="region_id")
    private Region region;

    @OneToMany(mappedBy="depart")
    private Set<Poste> postes;

    public Depart() {
        this("");
    }

    public Depart( String name ) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StringProperty getNameProperty() {
        return new SimpleStringProperty(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Set<Poste> getPostes() {
        return postes;
    }

    public void setPostes(Set<Poste> postes) {
        this.postes = postes;
    }

    public void removePostes( Session session ) {

        Iterator<Poste> iterator = postes.iterator();
        while ( iterator.hasNext() ) {

            session.remove( iterator.next() );
        }
    }
}
