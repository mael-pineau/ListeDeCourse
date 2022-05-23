package com.example.listecourse.Entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Unite")
public class Unite {

    @DatabaseField( generatedId = true )
    private int id;

    @DatabaseField( columnName="libelle")
    private String libelle;

    public Unite(int id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public Unite() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }

    @Override
    public boolean equals(Object otherUnite) {
        boolean same = false;
        if (otherUnite instanceof Unite) {
            same = this.getId() == ((Unite) otherUnite).getId();
        }
        return same;
    }
}
