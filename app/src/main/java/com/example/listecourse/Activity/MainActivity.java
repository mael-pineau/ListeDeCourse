package com.example.listecourse.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.listecourse.Activity.Produit.ProduitActivity;
import com.example.listecourse.Tools.DatabaseLinker;
import com.example.listecourse.Entity.ListeCourse;
import com.example.listecourse.Entity.Produit;
import com.example.listecourse.Entity.Produit_listeCourse;
import com.example.listecourse.Entity.Produit_recette;
import com.example.listecourse.Entity.Recette;
import com.example.listecourse.R;
import com.j256.ormlite.dao.Dao;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button buttonCourse;
    private Button buttonProduit;

    private Dao<Produit, Integer> daoProduit;
    private Dao<Recette, Integer> daoRecette;
    private Dao<ListeCourse, Integer> daoListeCourse;

    private Dao<Produit_recette, Integer> daoProduit_recette;
    private Dao<Produit_listeCourse, Integer> daoProduit_listeCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonCourse = findViewById(R.id.listeDeCourse);
        buttonProduit = findViewById(R.id.produit);

        // Set light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        buttonCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent monIntent = new Intent(MainActivity.this, ProduitActivity.class);
                startActivity(monIntent);
            }
        });

        buttonProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent monIntent = new Intent(MainActivity.this, ProduitActivity.class);
                startActivity(monIntent);
            }
        });


        //this.deleteDatabase("listeCourse.db");

        DatabaseLinker linker = new DatabaseLinker(this);
        try {
            daoProduit = linker.getDao( Produit.class );
            daoRecette = linker.getDao( Recette.class );
            daoListeCourse = linker.getDao( ListeCourse.class );

            daoProduit_recette = linker.getDao( Produit_recette.class );
            daoProduit_listeCourse = linker.getDao( Produit_listeCourse.class );

        } catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();

        List<Produit> produits = null;
        List<Recette> recettes = null;
        List<ListeCourse> listeCourses = null;

        List<Produit_recette> produit_recettes = null;
        List<Produit_listeCourse> produit_listeCourses = null;

        try {
            produits = daoProduit.queryForAll();
            recettes = daoRecette.queryForAll();
            listeCourses = daoListeCourse.queryForAll();

            produit_recettes = daoProduit_recette.queryForAll();
            produit_listeCourses = daoProduit_listeCourse.queryForAll();
        } catch (java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        for (Produit produit : produits) {
            Log.i("I found a product", "Son libelle : "+produit.getLibelle());
        }

        Log.i("", "-----------------------------------");

        for (Recette recette : recettes) {
            Log.i("I found a recette", "Son libelle : "+recette.getLibelle());
        }

        Log.i("", "-----------------------------------");

        for (ListeCourse listeCourse : listeCourses) {
            Log.i("I found a listeCourse", "Son libelle : "+listeCourse.getLibelle());
        }
    }
}