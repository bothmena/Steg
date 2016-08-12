package com.bothmena.entity;

import org.hibernate.Session;

import javax.persistence.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Aymen Ben Othmen on 19/07/16.
 */

@Entity
@Table(name = "region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy="region")
    private Set<Depart> departs;

    public Region() {
        this("");
    }

    public Region(String name) {
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

    public void setName(String name) {
        this.name = name;
    }

    public Set<Depart> getDeparts() {
        return departs;
    }

    public void removeDeparts( Session session ) {

        Iterator<Depart> iterator = departs.iterator();
        while ( iterator.hasNext() ) {

            Depart currentDepart = iterator.next();
            currentDepart.removePostes( session );
            session.remove( currentDepart );
        }
    }

    public void setDeparts(Set<Depart> departs) {
        this.departs = departs;
    }
}
