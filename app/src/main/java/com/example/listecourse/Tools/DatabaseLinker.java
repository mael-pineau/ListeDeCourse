package com.example.listecourse.Tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.listecourse.Entity.ListeCourse;
import com.example.listecourse.Entity.Produit;
import com.example.listecourse.Entity.Produit_listeCourse;
import com.example.listecourse.Entity.Produit_recette;
import com.example.listecourse.Entity.Recette;
import com.example.listecourse.Entity.Unite;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseLinker extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "listeCourse.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseLinker( Context context ) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            // Create database tables
            TableUtils.createTable( connectionSource, Produit.class );
            TableUtils.createTable( connectionSource, Recette.class );
            TableUtils.createTable( connectionSource, ListeCourse.class );
            TableUtils.createTable( connectionSource, Unite.class );

            TableUtils.createTable( connectionSource, Produit_recette.class );
            TableUtils.createTable( connectionSource, Produit_listeCourse.class );

            // Create DAOs
            Dao<Produit, Integer> daoProduit = this.getDao( Produit.class );
            Dao<Recette, Integer> daoRecette = this.getDao( Recette.class );
            Dao<ListeCourse, Integer> daoListeCourse = this.getDao( ListeCourse.class );
            Dao<Unite, Integer> daoUnite = this.getDao( Unite.class );

            Dao<Produit_recette, Integer> daoProduit_recette = this.getDao( Produit_recette.class );
            Dao<Produit_listeCourse, Integer> daoProduit_listeCourse = this.getDao( Produit_listeCourse.class );

            // Create entity tests
            // Main tables
            Produit produit1 = new Produit();
            produit1.setLibelle("Kinder Maxi");

            Produit produit2 = new Produit();
            produit2.setLibelle("Oeufs");

            Produit produit3 = new Produit();
            produit3.setLibelle("Lait");

            Produit produit4 = new Produit();
            produit4.setLibelle("Farine");

            Unite unite1 = new Unite();
            unite1.setLibelle("g");

            Unite unite2 = new Unite();
            unite2.setLibelle("kg");

            Unite unite3 = new Unite();
            unite3.setLibelle("ml");

            Unite unite4 = new Unite();
            unite4.setLibelle("L");

            Unite unite5 = new Unite();
            unite5.setLibelle("Unité");

            Recette recette1 = new Recette();
            recette1.setLibelle("Gateau au chocolat");

            Recette recette2 = new Recette();
            recette2.setLibelle("Crepes");

            ListeCourse listeCourse1 = new ListeCourse();
            listeCourse1.setLibelle("Liste de course de lundi");

            // Secondary tables
            Produit_recette produit_recette1 = new Produit_recette();
            produit_recette1.setProduit(produit2);
            produit_recette1.setRecette(recette2);
            produit_recette1.setUnite(unite5);
            produit_recette1.setVolume(1);
            produit_recette1.setQuantite(6);

            Produit_recette produit_recette2 = new Produit_recette();
            produit_recette2.setProduit(produit3);
            produit_recette2.setRecette(recette2);
            produit_recette2.setUnite(unite4);
            produit_recette2.setVolume(1);
            produit_recette2.setQuantite(1);

            Produit_recette produit_recette3 = new Produit_recette();
            produit_recette3.setProduit(produit4);
            produit_recette3.setRecette(recette2);
            produit_recette3.setUnite(unite2);
            produit_recette3.setVolume(1);
            produit_recette3.setQuantite(1);

            Produit_listeCourse produit_listeCourse1 = new Produit_listeCourse();
            produit_listeCourse1.setListeCourse(listeCourse1);
            produit_listeCourse1.setProduit(produit1);
            produit_listeCourse1.setUnite(unite5);
            produit_listeCourse1.setVolume(1);
            produit_listeCourse1.setQuantite(10);

            daoProduit.create(produit1);
            daoProduit.create(produit2);
            daoProduit.create(produit3);
            daoProduit.create(produit4);

            daoRecette.create(recette1);
            daoRecette.create(recette2);

            daoUnite.create(unite1);
            daoUnite.create(unite2);
            daoUnite.create(unite3);
            daoUnite.create(unite4);
            daoUnite.create(unite5);

            daoListeCourse.create(listeCourse1);

            daoProduit_recette.create(produit_recette1);
            daoProduit_recette.create(produit_recette2);
            daoProduit_recette.create(produit_recette3);

            daoProduit_listeCourse.create(produit_listeCourse1);

            Log.i( "DATABASE", "onCreate invoked" );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't create Database", exception );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i( "DATABASE", "onUpgrade invoked" );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't upgrade Database", exception );
        }
    }
}