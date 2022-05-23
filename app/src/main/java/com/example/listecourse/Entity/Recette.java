package com.example.listecourse.Entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Recette")
public class Recette {

    @DatabaseField( generatedId = true )
    private int id;

    @DatabaseField( columnName="libelle")
    private String libelle;

    public Recette(int id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public Recette() {
    }

    public int getId() {
        return id;
    }

    public void setId(int idRecette) {
        this.id = idRecette;
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
