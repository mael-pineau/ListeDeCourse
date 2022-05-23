package com.example.listecourse.Entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Produit_recette")
public class Produit_recette {

    @DatabaseField( generatedId = true )
    private int id;

    @DatabaseField( columnName="volume", canBeNull = true)
    private float volume;

    @DatabaseField( columnName="quantite")
    private int quantite;

    @DatabaseField( canBeNull = false, foreign = true, foreignColumnName = "id", foreignAutoCreate = true )
    private Produit produit;

    @DatabaseField( canBeNull = false, foreign = true, foreignColumnName = "id", foreignAutoCreate = true )
    private Unite unite;

    @DatabaseField( canBeNull = false, foreign = true, foreignColumnName = "id", foreignAutoCreate = true )
    public Recette recette;

    public Produit_recette(int id, float volume, int quantite, Produit produit, Unite unite, Recette recette) {
        this.id = id;
        this.volume = volume;
        this.quantite = quantite;
        this.produit = produit;
        this.unite = unite;
        this.recette = recette;
    }

    public Produit_recette() {

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

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Unite getUnite() {
        return unite;
    }

    public void setUnite(Unite unite) {
        this.unite = unite;
    }

    public Recette getRecette() {
        return recette;
    }

    public void setRecette(Recette recette) {
        this.recette = recette;
    }
}
