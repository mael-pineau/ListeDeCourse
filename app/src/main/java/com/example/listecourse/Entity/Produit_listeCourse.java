package com.example.listecourse.Entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Produit_listeCourse")
public class Produit_listeCourse {

    @DatabaseField( generatedId = true )
    private int id;

    @DatabaseField( columnName="volume")
    private float volume;

    @DatabaseField( columnName="quantite")
    private int quantite;

    @DatabaseField( columnName="isTicked")
    private boolean isTicked;

    @DatabaseField( canBeNull = false, foreign = true, foreignColumnName = "id", foreignAutoCreate = true )
    private Unite unite;

    @DatabaseField( canBeNull = false, foreign = true, foreignColumnName = "id", foreignAutoCreate = true )
    private ListeCourse listeCourse;

    @DatabaseField( canBeNull = false, foreign = true, foreignColumnName = "id", foreignAutoCreate = true )
    private Produit produit;

    public Produit_listeCourse(int id, float volume, int quantite, boolean isTicked, Unite unite, ListeCourse listeCourse, Produit produit) {
        this.id = id;
        this.volume = volume;
        this.quantite = quantite;
        this.isTicked = isTicked;
        this.unite = unite;
        this.listeCourse = listeCourse;
        this.produit = produit;
    }

    public Produit_listeCourse() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Unite getUnite() {
        return unite;
    }

    public void setUnite(Unite unite) {
        this.unite = unite;
    }

    public ListeCourse getListeCourse() {
        return listeCourse;
    }

    public void setListeCourse(ListeCourse listeCourse) {
        this.listeCourse = listeCourse;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public boolean isTicked() {
        return isTicked;
    }

    public void setTicked(boolean ticked) {
        isTicked = ticked;
    }
}
