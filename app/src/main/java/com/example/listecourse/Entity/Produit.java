package com.example.listecourse.Entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Produit")
public class Produit {

    @DatabaseField( generatedId = true )
    private int id;

    @DatabaseField( columnName="libelle")
    private String libelle;

    public Produit(int id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public Produit() {

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
    public boolean equals(Object otherProduit) {
        boolean same = false;
        if (otherProduit instanceof Produit) {
            same = this.getId() == ((Produit) otherProduit).getId();
        }
        return same;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
