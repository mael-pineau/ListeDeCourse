package com.example.listecourse.Entity;

import com.j256.ormlite.field.DatabaseField;

public class ListeCourse {

    @DatabaseField( generatedId = true )
    private int id;

    @DatabaseField( columnName="libelle")
    private String libelle;

    public ListeCourse(int id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public ListeCourse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int idListeCourse) {
        this.id = idListeCourse;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
